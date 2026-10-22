package com.kimyanu.blog.service;

import com.kimyanu.blog.exceptions.custom.MemberNotFoundException;
import com.kimyanu.blog.model.Member;
import com.kimyanu.blog.repository.MemberRepository;
import com.kimyanu.blog.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void logout(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        // 로그아웃 시, 해당 회원의 모든 refresh token을 삭제한다.
        refreshTokenRepository.deleteAllByMemberId(memberId);

        // 토큰 발급 시각은 초 단위라, 같은 초에 발급된 토큰까지 막으려면 다음 초로 돌린다
        member.setTokenInvalidBefore(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusSeconds(1));
    }
}
