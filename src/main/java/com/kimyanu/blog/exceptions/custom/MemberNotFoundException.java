package com.kimyanu.blog.exceptions.custom;

public final class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException() {
        super("해당 ID의 회원을 찾을 수 없습니다.");
    }
}
