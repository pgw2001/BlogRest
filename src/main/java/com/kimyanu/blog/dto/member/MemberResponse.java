package com.kimyanu.blog.dto.member;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.kimyanu.blog.enums.MemberRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonPropertyOrder({"id", "email", "nickname", "role", "createdAt", "postCount"})
public class MemberResponse {
    private Long id;
    private String email;
    private String nickname;
    private MemberRole role;
    private LocalDateTime createdAt;
    private Long postCount;  // 이 회원이 쓴 글 수(감춘 글 포함)
}
