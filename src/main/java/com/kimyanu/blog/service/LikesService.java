package com.kimyanu.blog.service;

public interface LikesService {
    void createLike(Long memberId, Long postId);

    void deleteLike(Long memberId, Long postId);
}
