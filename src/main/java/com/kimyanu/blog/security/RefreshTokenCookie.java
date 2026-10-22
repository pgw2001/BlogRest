package com.kimyanu.blog.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RefreshTokenCookie {
    private static final String NAME = "refreshToken";
    private final Long refreshExpirationMillis;

    public RefreshTokenCookie(@Value("${jwt.refresh-expiration}")Long refreshExpirationMillis) {
        this.refreshExpirationMillis = refreshExpirationMillis;
    }

    public ResponseCookie create(String token) {
        return ResponseCookie.from(NAME, token)
                // 자바스크립트가 읽을 수 없다
                .httpOnly(true)
                // HTTPS로만 전송한다
                .secure(true)
                // 다른 사이트에서 시작된 요청에는 붙이 않는다
                .sameSite("Strict")
                // 재발급 요청에만 붙는다. 노출범위를 우리가 좁힌다
                .path("/api/auth/refresh")
                .maxAge(Duration.ofMillis(refreshExpirationMillis))
                .build();
    }
}
