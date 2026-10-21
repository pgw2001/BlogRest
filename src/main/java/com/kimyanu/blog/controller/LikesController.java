package com.kimyanu.blog.controller;

import com.kimyanu.blog.dto.likes.LikesResponse;
import com.kimyanu.blog.security.CustomUserDetails;
import com.kimyanu.blog.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/posts/{postId}/likes")
    public ResponseEntity<LikesResponse> createLikes(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long postId) {

        LikesResponse response = likesService.createLike(userDetails.getMemberId(), postId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/posts/{postId}/likes")
    public ResponseEntity<LikesResponse> deleteLikes(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long postId) {

        LikesResponse response = likesService.deleteLike(userDetails.getMemberId(), postId);

        return ResponseEntity.ok(response);
    }
}
