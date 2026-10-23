package com.kimyanu.blog.controller;

import com.kimyanu.blog.config.AppConstant;
import com.kimyanu.blog.dto.common.PageResponse;
import com.kimyanu.blog.dto.post.PostRequest;
import com.kimyanu.blog.dto.post.PostResponse;
import com.kimyanu.blog.dto.post.PostVisibilityRequest;
import com.kimyanu.blog.enums.MemberRole;
import com.kimyanu.blog.security.CustomUserDetails;
import com.kimyanu.blog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @GetMapping("/public/posts")
    public ResponseEntity<PageResponse<PostResponse>> getAllPosts(
            @RequestParam (value = "pageNumber", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer pageNumber,
            @RequestParam (value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam (value = "sortBy", defaultValue = AppConstant.SORT_POST_BY) String sortBy,
            @RequestParam (value = "sortOrder", defaultValue = AppConstant.SORT_POST_ASC) String sortOrder
            ) {
        PageResponse<PostResponse> posts = postService.getAllPosts(pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/admin/posts")
    public ResponseEntity<PageResponse<PostResponse>> getAllPostsForAdmin(
            @RequestParam (value = "pageNumber", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer pageNumber,
            @RequestParam (value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam (value = "sortBy", defaultValue = AppConstant.SORT_POST_BY) String sortBy,
            @RequestParam (value = "sortOrder", defaultValue = AppConstant.SORT_POST_ASC) String sortOrder
    ) {
        PageResponse<PostResponse> posts = postService.getAllPostsForAdmin(pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/public/posts/{postId}")
    public ResponseEntity<PostResponse> getPostById(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long postId) {

        Long memberId = (userDetails != null) ? userDetails.getMemberId() : null;
        MemberRole role = (userDetails != null) ? userDetails.getRole() : null;

        PostResponse post = postService.getPostById(memberId, postId, role);

        return ResponseEntity.ok(post);
    }

    @GetMapping("/members/me/posts")
    public ResponseEntity<PageResponse<PostResponse>> getMyPosts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam (value = "pageNumber", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer pageNumber,
            @RequestParam (value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam (value = "sortBy", defaultValue = AppConstant.SORT_POST_BY) String sortBy,
            @RequestParam (value = "sortOrder", defaultValue = AppConstant.SORT_POST_ASC) String sortOrder) {

        PageResponse<PostResponse> myPosts = postService.getMyPosts(userDetails.getMemberId(), pageNumber, pageSize, sortBy, sortOrder);

        return ResponseEntity.ok(myPosts);
    }

    @PostMapping("/posts")
    public ResponseEntity<PostResponse> createPost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody PostRequest postRequest) {
        PostResponse postResponse = postService.createPost(postRequest, userDetails.getMemberId());
        return ResponseEntity.ok(postResponse);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId,
            @Valid @RequestBody PostRequest postRequest) {

        PostResponse updatedPost = postService.updatePost(userDetails.getMemberId(),postId, postRequest);

        return ResponseEntity.ok(updatedPost);
    }

    @PatchMapping("/admin/posts/{postId}/visibility")
    @Transactional
    public ResponseEntity<PostResponse> updatePostVisibility(
            @PathVariable Long postId,
            @Valid @RequestBody PostVisibilityRequest postVisibilityRequest) {

        PostResponse response = postService.updatePostVisibility(postId, postVisibilityRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/posts/me/{postId}")
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long postId) {

        postService.deletePost(userDetails.getMemberId(), postId);

        return ResponseEntity.noContent().build();
    }


}