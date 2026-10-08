package com.kimyanu.blog.controller;

import com.kimyanu.blog.dto.post.PostDto;
import com.kimyanu.blog.dto.post.PostRequest;
import com.kimyanu.blog.dto.post.PostResponse;
import com.kimyanu.blog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/public/posts")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostRequest postRequest, @RequestParam("memberId") Long memberId) {
        PostDto postDto = postService.createPost(postRequest, memberId);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/public/posts")
    public ResponseEntity<PostResponse> getAllPosts() {
        PostResponse posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/public/posts/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId) {

        PostDto post = postService.getPostById(postId);

        return ResponseEntity.ok(post);
    }

    //TODO: 스프링 시큐리티 도입 시 작성자 본인 확인 필요
    @DeleteMapping("/public/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {

        postService.deletePost(postId);

        return ResponseEntity.noContent().build();
    }

    // TODO: 수정하기 (PUT /public/posts/{postId}), PostDto 반환하도록 구현 필요
    // TODO: 스프링 시큐리티 도입 후 작성자 본인 확인 로직 추가 필요
    @PutMapping("/public/posts/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long postId, @Valid @RequestBody PostRequest postRequest) {

        PostDto updatedPost = postService.updatePost(postId, postRequest);

        return ResponseEntity.ok(updatedPost);
    }


}
