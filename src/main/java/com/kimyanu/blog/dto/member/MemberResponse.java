package com.kimyanu.blog.dto.member;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonPropertyOrder({"id", "email", "nickname", "role", "createdAt"})
public class MemberResponse {
    private Long id;
    private String email;
    private String nickname;
    private String role;
    private LocalDateTime createdAt;
}
