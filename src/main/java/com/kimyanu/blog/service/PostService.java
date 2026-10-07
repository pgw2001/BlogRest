package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.post.PostDto;
import com.kimyanu.blog.dto.post.PostRequest;

public interface PostService {
    PostDto createPost(PostRequest postRequest, Long memberId);
}
