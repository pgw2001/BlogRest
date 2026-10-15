package com.kimyanu.blog.controller;

import com.kimyanu.blog.config.AppConstant;
import com.kimyanu.blog.dto.comment.CommentDto;
import com.kimyanu.blog.dto.comment.CommentRequest;
import com.kimyanu.blog.dto.comment.CommentResponse;
import com.kimyanu.blog.security.CustomUserDetails;
import com.kimyanu.blog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/public/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> getAllComments(
            @PathVariable Long postId,
            @RequestParam (value = "pageNumber", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer pageNumber,
            @RequestParam (value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam (value = "sortBy", defaultValue = AppConstant.SORT_POST_BY) String sortBy,
            @RequestParam (value = "sortOrder", defaultValue = AppConstant.SORT_POST_ASC) String sortOrder) {

        CommentResponse response = commentService.getCommentsByPostId(
                postId,
                pageNumber,
                pageSize,
                sortBy,
                sortOrder);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest commentRequest) {

        CommentDto commentDto = commentService.createComment(userDetails.getMemberId(), postId, commentRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(commentDto);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest commentRequest) {

        CommentDto updatedComment = commentService.updateComment(userDetails.getMemberId(), commentId, commentRequest);

        return ResponseEntity.ok().body(updatedComment);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long commentId) {
        commentService.deleteComment(userDetails.getMemberId(), commentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
