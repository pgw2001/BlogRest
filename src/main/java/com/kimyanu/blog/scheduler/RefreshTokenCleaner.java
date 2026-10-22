package com.kimyanu.blog.scheduler;

import com.kimyanu.blog.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenCleaner {
    private final RefreshTokenService refreshTokenService;

    // 초 | 분 | 시 | 일 | 월 | 요일
    // 0 0 4 * * * // 매일 새벽 4시
    // 0/30 * * * * * : 30초마다 실행
    @Scheduled(cron = "0 0 4 * * *")
    @Scheduled(fixedRate = 30000)
    public void cleanExpiredTokens() {
        int deletedCount = refreshTokenService.deleteExpired();
        System.out.println("Cleaning expired refresh tokens... Deleted count: " + deletedCount);
    }


}
