package com.kimyanu.blog.dto.member;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberAdminResponse {
    private Long id;
    private String email;
    private String nickname;
    private String role;
    private LocalDateTime createdAt;
}
