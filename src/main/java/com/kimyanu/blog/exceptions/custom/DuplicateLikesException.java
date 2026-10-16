package com.kimyanu.blog.exceptions.custom;

public final class DuplicateLikesException extends RuntimeException {
    public DuplicateLikesException()
    {
        super("이미 좋아요를 누른 게시글입니다");
    }
}
