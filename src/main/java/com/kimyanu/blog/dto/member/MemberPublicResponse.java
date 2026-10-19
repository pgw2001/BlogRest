package com.kimyanu.blog.dto.member;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberPublicResponse {
    private String nickname;
    private LocalDateTime createdAt;

}
