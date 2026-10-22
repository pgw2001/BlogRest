package com.kimyanu.blog.controller;

import com.kimyanu.blog.dto.auth.LoginRequest;
import com.kimyanu.blog.dto.auth.LoginResponse;
import com.kimyanu.blog.dto.auth.RefreshResult;
import com.kimyanu.blog.exceptions.custom.InvalidRefreshTokenException;
import com.kimyanu.blog.security.CustomUserDetails;
import com.kimyanu.blog.security.JwtProvider;
import com.kimyanu.blog.security.RefreshTokenCookie;
import com.kimyanu.blog.service.AuthService;
import com.kimyanu.blog.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenCookie refreshTokenCookie;

    @PostMapping("/public/auth/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

        // 이미 여기까지 코드가 도달했다는 것은, 스프링 시큐리티 기본 필터들을 통과했다는 것을 의미한다.
        // 그런데, 사용자가 입력한 이메일과 비밀번호가 맞는지 확인하고 인증을 처리하는 과정이 필요해지게 됨.
        // 그래서 이 처리하는 과정을 내가 직접 레포지토리 및 서비스 계층을 호출해서 처리할 수도 있지만,
        // 이렇게 되게 되면, 스프링 시큐리티는 인증이 처리되었다는 사실을 알 수 없게 된다.

        // 그래서 스프링 시큐리티에게 다시 인증을 처리하도록 요청을 해야 하는데,
        // 이때, AuthenticationManager를 사용하면 된다.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // 인증된 결과에서 회원 정보를 꺼낸다.
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // 회원 정보로 액세스 토큰을 만든다.
        String token = jwtProvider.createToken(Objects.requireNonNull(userDetails));

        // 리프레시 토큰을 만든다.
        String refreshToken = refreshTokenService.issued(userDetails.getMemberId());
        ResponseCookie cookie = refreshTokenCookie.create(refreshToken);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResponse(token, "Bearer"));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMemberId();

        // 로그아웃 시, 토큰 무효화 처리
        authService.logout(memberId);

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.expire().toString())
                .build();
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<LoginResponse> refreshToken(
            @CookieValue(value = RefreshTokenCookie.NAME , required = false) String refreshToken
    ) {
        if (refreshToken == null) {
            throw new InvalidRefreshTokenException();
        }

        // 리프레시 토큰을 검증하고 새로운 액세스 토큰을 발급한다.
        RefreshResult result = refreshTokenService.refresh(refreshToken);
        String token = jwtProvider.createToken(result.userDetails());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.create(result.refreshToken()).toString())
                .body(new LoginResponse(token, "Bearer"));
    }

}
