package com.kimyanu.blog.dto.member;

import com.kimyanu.blog.enums.MemberRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberAdminResponse {
    private Long id;
    private String email;
    private String nickname;
    private MemberRole role;
    private LocalDateTime createdAt;
}
