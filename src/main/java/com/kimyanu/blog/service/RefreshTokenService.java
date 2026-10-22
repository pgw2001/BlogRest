package com.kimyanu.blog.service;

import com.kimyanu.blog.controller.InvalidRefreshTokenException;
import com.kimyanu.blog.dto.auth.RefreshResult;
import com.kimyanu.blog.model.Member;
import com.kimyanu.blog.model.RefreshToken;
import com.kimyanu.blog.repository.MemberRepository;
import com.kimyanu.blog.repository.RefreshTokenRepository;
import com.kimyanu.blog.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final Long refreshExpirationMillis;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               MemberRepository memberRepository,
                               @Value("${jwt.refresh-expiration}")Long refreshExpirationMillis) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberRepository = memberRepository;
        this.refreshExpirationMillis = refreshExpirationMillis;
    }

    @Transactional
    public String issued(Long memberId) {
        // getReferenceById는 select를 실행하지 않고, 회원 번호만 가진 프록시 객체를 돌려준다. member_id를 저장하는 데에는 번호만 있으면 된다.
        Member member = memberRepository.getReferenceById(memberId);

        RefreshToken refreshToken= new RefreshToken();
        refreshToken.setMember(member);

        // 의미 없는 임의의 문자열이다. 값 자체로는 아무것도 알 수 없다.
        refreshToken.setToken(java.util.UUID.randomUUID().toString());
        refreshToken.setExpiresAt(java.time.LocalDateTime.now().plus(refreshExpirationMillis, ChronoUnit.MILLIS));
        refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }

    @Transactional
    public RefreshResult refresh(String token) {

        // 1. 토큰을 찾는다. 없으면 발급한 적이 없거나, 이미 사용했거나, 로그아웃으로 삭제된 토큰이다.
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(InvalidRefreshTokenException::new);

        // 2. 만료되었는지 확인한다. 만료 시각은 토큰 안이 아니라 우리 컬럼에 적혀 있다.
        //    만료된 토큰은 더 쓸 일이 없으므로 지운다.
        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.deleteByToken(token);
            throw new InvalidRefreshTokenException();
        }

        // 3. 토큰을 새 토큰으로 교체한다. 한 번 쓰면 끝이다.
        refreshTokenRepository.deleteByToken(token);
        // 4. 토큰의 주인으로 CustomUserDetails를 만든다. 컨트롤러가 이것으로 액세스 토큰을 발급한다.
        Member member = refreshToken.getMember();
        CustomUserDetails userDetails = new CustomUserDetails(member);

        // 5. 방금 쓴 토큰은 버린다. 한 번 쓰면 끝이다.
        String newRefreshToken = issued(member.getId());

        return new RefreshResult(userDetails, newRefreshToken);
    }

    @Transactional
    public int deleteExpired() {
        return refreshTokenRepository.deleteExpired(LocalDateTime.now());
    }


}
