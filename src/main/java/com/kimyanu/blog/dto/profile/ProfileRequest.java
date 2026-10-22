package com.kimyanu.blog.dto.profile;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ProfileRequest {

    @Size(max = 500, message = "자기소개는 500자 이하로 입력해주세요.")
    private String bio;
    @Size(max = 255, message = "이미지 주소는 500자 이하로 입력해주세요.")
    private String profileImage;
    @Size(max = 255, message = "SNS 주소는 500자 이하로 입력해주세요.")
    private String snsLink;

    public void setBio(String bio) {
        this.bio = bio == null ? null : bio.trim();
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage == null ? null : profileImage.trim();
    }

    public void setSnsLink(String snsLink) {
        this.snsLink = snsLink == null ? null : snsLink.trim();
    }
}
