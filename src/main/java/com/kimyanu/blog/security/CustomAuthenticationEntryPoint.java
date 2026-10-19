package com.kimyanu.blog.security;

import com.kimyanu.blog.exceptions.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

// 인증되지 않은 사용자가 보호된 리소스에 접근하려고 할 때 발생하는 예외를 처리하는 클래스 (401)
@Component
@RequiredArgsConstructor
@NullMarked
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        // 1. HTTP 응답 상태 코드를 401 FORBIDDEN으로 설정합니다.
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 2. 응답의 Content-Type을 application/json으로 설정합니다.
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 3. 응답의 문자 인코딩을 UTF-8로 설정합니다.
        response.setCharacterEncoding("UTF-8");

        // 4. ErrorResponse 객체를 생성하여 JSON의 형식으로 응답 본문에 작성합니다.
        objectMapper.writeValue(response.getWriter(),
                new ErrorResponse("인증에 실패했습니다."));
    }
}
