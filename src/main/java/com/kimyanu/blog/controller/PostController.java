package com.kimyanu.blog.controller;

import com.kimyanu.blog.config.AppConstant;
import com.kimyanu.blog.dto.post.PostDto;
import com.kimyanu.blog.dto.post.PostRequest;
import com.kimyanu.blog.dto.post.PostResponse;
import com.kimyanu.blog.security.CustomUserDetails;
import com.kimyanu.blog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    // TODO: 내 게시글만 조회하는 API를 만들어야 한다. (인증 필요)
    // TODO: GET /api/members/me/posts, PostResponse 반환, 공개/비공개 상관하지 않고 모두 조회
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

    @DeleteMapping("/posts/me/{postId}")
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long postId) {

        postService.deletePost(userDetails.getMemberId(), postId);

        return ResponseEntity.noContent().build();
    }


}