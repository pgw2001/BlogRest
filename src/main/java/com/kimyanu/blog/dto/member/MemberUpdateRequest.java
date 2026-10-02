package com.kimyanu.blog.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateRequest {
    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;
    private String password;
}
