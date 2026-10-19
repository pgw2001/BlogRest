package com.kimyanu.blog.security;

import com.kimyanu.blog.exceptions.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

// 인증된 사용자가 권한이 없는 리소스에 접근하려고 할 때 발생하는 예외를 처리하는 클래스 (403)
@Component
@RequiredArgsConstructor
@NullMarked
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    // ObjectMapper는 JSON 직렬화 및 역직렬화를 처리하는 데 사용됩니다.
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // 1. HTTP 응답 상태 코드를 403 FORBIDDEN으로 설정합니다.
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // 2. 응답의 Content-Type을 application/json으로 설정합니다.
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 3. 응답의 문자 인코딩을 UTF-8로 설정합니다.
        response.setCharacterEncoding("UTF-8");

        // 4. ErrorResponse 객체를 생성하여 JSON의 형식으로 응답 본문에 작성합니다.
        objectMapper.writeValue(response.getWriter(),
                new ErrorResponse("접근 권한이 없습니다."));
    }
}
