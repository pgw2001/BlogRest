package com.kimyanu.blog.exceptions.custom;

public final class LikesNotFoundException extends RuntimeException {
    public LikesNotFoundException() {
        super("좋아요를 누르지 않은 게시글입니다.");
    }
}
