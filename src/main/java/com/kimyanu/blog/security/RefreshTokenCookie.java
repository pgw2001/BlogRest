package com.kimyanu.blog.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RefreshTokenCookie {
    public static final String NAME = "refreshToken";
    private static final String PATH = "/api/public/auth/refresh";

    private final Long refreshExpirationMillis;


    public RefreshTokenCookie(@Value("${jwt.refresh-expiration}")Long refreshExpirationMillis) {
        this.refreshExpirationMillis = refreshExpirationMillis;
    }

    private ResponseCookie.ResponseCookieBuilder base(String value) {
        return ResponseCookie.from(NAME, value)
                // 자바스크립트가 읽을 수 없다
                .httpOnly(true)
                // HTTPS로만 전송한다
                .secure(true)
                // 프론트(vercel.app)와 서버(cloudtype.app)가 다른 사이트라 None 이어야 쿠키가 붙는다.
                // None 은 secure(true)가 있어야 브라우저가 받아 준다.
                .sameSite("None")
                // 재발급 요청에만 붙는다. 노출범위를 우리가 좁힌다
                .path(PATH);
    }

    public ResponseCookie create(String token) {
        return base(token).maxAge(Duration.ofMillis(refreshExpirationMillis)).build();
    }

    public ResponseCookie expire() {
        return base("").maxAge(0).build();
    }
}
