package com.kimyanu.blog.dto.profile;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"memberId", "nickname", "bio", "profileImage", "snsLink"})
public class ProfileResponse {
    private Long memberId;
    private String nickname;
    private String bio;
    private String profileImage;
    private String snsLink;
}
