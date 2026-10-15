package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.post.PostDto;
import com.kimyanu.blog.dto.post.PostRequest;
import com.kimyanu.blog.dto.post.PostResponse;
import com.kimyanu.blog.dto.post.PostVisibilityRequest;

public interface PostService {
    PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    PostDto getPostById(Long postId);

    PostResponse getMyPosts(Long memberId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    PostDto createPost(PostRequest postRequest, Long memberId);

    PostDto updatePost(Long memberId, Long postId, PostRequest postRequest);

    void deletePost(Long memberId, Long postId);

    PostDto updatePostVisibility(Long postId, PostVisibilityRequest postVisibilityRequest);
}
