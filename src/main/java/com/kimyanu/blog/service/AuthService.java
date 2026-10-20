package com.kimyanu.blog.service;

import com.kimyanu.blog.exceptions.custom.MemberNotFoundException;
import com.kimyanu.blog.model.Member;
import com.kimyanu.blog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;

    @Transactional
    public void logout(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        // 토큰 발급 시각은 초 단위라, 같은 초에 발급된 토큰까지 막으려면 다음 초로 돌린다
        member.setTokenInvalidBefore(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusSeconds(1));
    }
}
