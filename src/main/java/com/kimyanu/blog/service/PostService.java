package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.post.PostDto;
import com.kimyanu.blog.dto.post.PostRequest;
import com.kimyanu.blog.dto.post.PostResponse;

public interface PostService {
    PostDto createPost(PostRequest postRequest, Long memberId);

    PostResponse getAllPosts();

    PostDto getPostById(Long postId);

    void deletePost(Long postId);

    PostDto updatePost(Long postId, PostRequest postRequest);
}
