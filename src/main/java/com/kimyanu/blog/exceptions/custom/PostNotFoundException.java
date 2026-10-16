package com.kimyanu.blog.exceptions.custom;

public final class PostNotFoundException extends RuntimeException {
    public PostNotFoundException() {

        super("게시글을 찾을 수 없습니다.");
    }
}
