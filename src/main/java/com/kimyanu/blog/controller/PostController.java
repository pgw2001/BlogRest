package com.kimyanu.blog.controller;

import com.kimyanu.blog.config.AppConstant;
import com.kimyanu.blog.dto.post.PostDto;
import com.kimyanu.blog.dto.post.PostRequest;
import com.kimyanu.blog.dto.post.PostResponse;
import com.kimyanu.blog.dto.post.PostVisibilityRequest;
import com.kimyanu.blog.security.CustomUserDetails;
import com.kimyanu.blog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/public/posts")
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam (value = "pageNumber", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer pageNumber,
            @RequestParam (value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam (value = "sortBy", defaultValue = AppConstant.SORT_POST_BY) String sortBy,
            @RequestParam (value = "sortOrder", defaultValue = AppConstant.SORT_POST_ASC) String sortOrder
            ) {
        PostResponse posts = postService.getAllPosts(pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/public/posts/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId) {

        PostDto post = postService.getPostById(postId);

        return ResponseEntity.ok(post);
    }

    @GetMapping("/members/me/posts")
    public ResponseEntity<PostResponse> getMyPosts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam (value = "pageNumber", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer pageNumber,
            @RequestParam (value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam (value = "sortBy", defaultValue = AppConstant.SORT_POST_BY) String sortBy,
            @RequestParam (value = "sortOrder", defaultValue = AppConstant.SORT_POST_ASC) String sortOrder) {

        PostResponse myPosts = postService.getMyPosts(userDetails.getMemberId(), pageNumber, pageSize, sortBy, sortOrder);

        return ResponseEntity.ok(myPosts);
    }

    @PostMapping("/posts")
    public ResponseEntity<PostDto> createPost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody PostRequest postRequest) {
        PostDto postDto = postService.createPost(postRequest, userDetails.getMemberId());
        return ResponseEntity.ok(postDto);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDto> updatePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId,
            @Valid @RequestBody PostRequest postRequest) {

        PostDto updatedPost = postService.updatePost(userDetails.getMemberId(),postId, postRequest);

        return ResponseEntity.ok(updatedPost);
    }

    // TODO: 게시글의 공개/비공개 여부를 변경하는 API
    // TODO: Put "/admin/posts/{postId}/visibility
    // TODO: PostVisibilityRequest 생성해서 isVisible 필드만 받도록 함, 이 값에 의해서 게시글의 공개/비공개 여부를 변경
    // TODO: PostResponse 응답
    @PutMapping("/admin/posts/{postId}/visibility")
    @Transactional
    public ResponseEntity<PostDto> updatePostVisibility(
            @PathVariable Long postId,
            @Valid @RequestBody PostVisibilityRequest postVisibilityRequest) {

        PostDto response = postService.updatePostVisibility(postId, postVisibilityRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/posts/me/{postId}")
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long postId) {

        postService.deletePost(userDetails.getMemberId(), postId);

        return ResponseEntity.noContent().build();
    }


}