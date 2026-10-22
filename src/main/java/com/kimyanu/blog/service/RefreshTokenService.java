package com.kimyanu.blog.service;

import com.kimyanu.blog.model.Member;
import com.kimyanu.blog.model.RefreshToken;
import com.kimyanu.blog.repository.MemberRepository;
import com.kimyanu.blog.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
