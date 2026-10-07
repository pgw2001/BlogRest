package com.kimyanu.blog.controller;

import com.kimyanu.blog.dto.post.PostDto;
import com.kimyanu.blog.dto.post.PostRequest;
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

    // TODO: 게시글 등록 (POST /api/public/posts) - memberId는 RequestParam으로 받음, PostRequest는 RequestBody로 받음, 204 no content 반환
    @PostMapping("/public/posts")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostRequest postRequest, @RequestParam("memberId") Long memberId) {
        PostDto postDto = postService.createPost(postRequest, memberId);
        return ResponseEntity.ok(postDto);
    }
    // TODO: 게시글 조회 (GET /api/public/posts) - 반환: PostResponse 객체

}
