package com.kimyanu.blog.exceptions.custom;

public class RoleException extends RuntimeException {

    public RoleException() {

        super("관리자는 자신의 역할을 변경할 수 없습니다.");
    }
}
