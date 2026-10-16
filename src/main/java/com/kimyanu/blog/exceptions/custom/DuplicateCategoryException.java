package com.kimyanu.blog.exceptions.custom;

import lombok.Getter;

@Getter
public final class DuplicateCategoryException extends RuntimeException {
    public DuplicateCategoryException() {
        super("이미 존재하는 카테고리입니다.");
    }

    public DuplicateCategoryException(String message) {
        super("이미 존재하는 카테고리입니다.(" + message + ")");
    }
    
}
