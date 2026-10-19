package com.kimyanu.blog.dto.member;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonPropertyOrder({"nickname", "createdAt"})
public class MemberPublicResponse {
    private String nickname;
    private LocalDateTime createdAt;

}
