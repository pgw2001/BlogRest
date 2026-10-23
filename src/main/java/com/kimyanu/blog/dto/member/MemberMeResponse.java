package com.kimyanu.blog.dto.member;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.kimyanu.blog.dto.auth.SocialAccountResponse;
import com.kimyanu.blog.dto.profile.ProfileResponse;
import com.kimyanu.blog.enums.MemberRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonPropertyOrder({"id", "email", "nickname", "role", "createdAt", "profile", "socialAccounts"})
public class MemberMeResponse {
    private Long id;
    private String email;
    private String nickname;
    private MemberRole role;
    private LocalDateTime createdAt;
    private ProfileResponse profile;
    private List<SocialAccountResponse> socialAccounts;
}
