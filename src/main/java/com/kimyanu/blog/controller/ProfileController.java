package com.kimyanu.blog.controller;

import com.kimyanu.blog.dto.profile.ProfileRequest;
import com.kimyanu.blog.dto.profile.ProfileResponse;
import com.kimyanu.blog.security.CustomUserDetails;
import com.kimyanu.blog.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/public/members/{memberId}/profile")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable Long memberId) {

        ProfileResponse response = profileService.getProfile(memberId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("members/me/profile")
    public ResponseEntity<ProfileResponse> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ProfileRequest profileRequest) {

        ProfileResponse response = profileService.updateProfile(userDetails.getMemberId(), profileRequest);

        return ResponseEntity.ok(response);
    }
}

