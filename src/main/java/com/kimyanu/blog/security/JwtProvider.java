package com.kimyanu.blog.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {
    private final SecretKey secretKey;
    private final Long expirationMills;

    public JwtProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration}") Long expirationMills) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expirationMills = expirationMills;
    }

    public String createToken(CustomUserDetails userDetails) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMills);

        return Jwts.builder()   // JWT 생성의 시작점
                .subject(String.valueOf(userDetails.getMemberId())) // 토큰의 고유 식별자 설정
                .claim("email", userDetails.getEmail()) // 개발자가 자유롭게 추가하고 싶은 커스텀 데이터
                .claim("role", userDetails.getRole())   // 개발자가 자유롭게 추가하고 싶은 커스텀 데이터
                .issuedAt(now) // 토큰 발급 시간
                .expiration(expiration) // 토큰 만료 시간
                .signWith(secretKey)    // 토큰 서명
                .compact(); // JWT 토큰 생성
    }
}
