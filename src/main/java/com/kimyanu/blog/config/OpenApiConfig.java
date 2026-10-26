package com.kimyanu.blog.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
            title = "블로그 API",
            version = "v1",
            description = "글, 댓글 좋아요, 회원, 카테고리를 다루는 블록 REST API입니다."
    ),
    security = @SecurityRequirement(name = "accessToken")
)
@SecurityScheme(
        name = "accessToken",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "로그인 응답의 accessToken 값을 그대로 붙여 넣습니다."
)
public class OpenApiConfig {
}
