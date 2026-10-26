package com.kimyanu.blog.controller;

import com.kimyanu.blog.controller.docs.ProfileControllerDocs;
import com.kimyanu.blog.dto.profile.ProfileRequest;
import com.kimyanu.blog.dto.profile.ProfileResponse;
import com.kimyanu.blog.security.CustomUserDetails;
import com.kimyanu.blog.service.ProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "프로필", description = "회원의 자기소개, 사진, SNS 주소를 보고 고칩니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProfileController implements ProfileControllerDocs {

    private final ProfileService profileService;

    @Override
    @GetMapping("/public/members/{memberId}/profile")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable Long memberId) {

        ProfileResponse response = profileService.getProfile(memberId);

        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping("members/me/profile")
    public ResponseEntity<ProfileResponse> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ProfileRequest profileRequest) {

        ProfileResponse response = profileService.updateProfile(userDetails.getMemberId(), profileRequest);

        return ResponseEntity.ok(response);
    }
}

