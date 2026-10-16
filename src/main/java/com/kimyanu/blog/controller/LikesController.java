package com.kimyanu.blog.controller;

import com.kimyanu.blog.security.CustomUserDetails;
import com.kimyanu.blog.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikesController {

    private final LikesService likesService;

    // TODO: 좋아요 기능
    // TODO: POST /api/posts/{postId}/likes
    // TODO: 로그인한 사용자만 좋아요를 누를 수 있도록 인증 필요
    // TODO: 이미 좋아요를 누른 경우에는 중복 좋아요 방지
    // TODO: 201 Created 응답 반환
    @PostMapping("/posts/{postId}/likes")
    public ResponseEntity<Void> createLikes(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long postId) {

        likesService.createLike(userDetails.getMemberId(), postId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // TODO: 좋아요 취소 기능
    // TODO: DELETE /api/posts/{postId}/likes
    // TODO: 로그인한 사용자만 좋아요를 취소할 수 있도록 인증 필요
    // TODO: 좋아요를 누르지 않은 경우에는 취소할 수 없도록 처리
    // TODO: 204 No Content 응답 반환
    @DeleteMapping("/posts/{postId}/likes")
    public ResponseEntity<Void> deleteLikes(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long postId) {

        likesService.deleteLike(userDetails.getMemberId(), postId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
