package com.kimyanu.blog.dto.member;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateRequest {
    @Size(min = 1, max = 20, message = "닉네임은 1자 이상 20자 이하로 작성해주세요.")
    private String nickname;
    @Size(min = 8, max = 64, message = "비밀번호는 8자 이상 64자 이하로 작성해주세요.")
    private String password;

    public void setPassword(String password) {
        System.out.println("Setting password: " + password);
        this.password = password == null ? null : password.trim();
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }
}