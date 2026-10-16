package com.kimyanu.blog.exceptions.custom;

public final class DuplicateMemberException extends RuntimeException {

    private DuplicateMemberException(String message) {

        super(message);
    }

    public static DuplicateMemberException duplicateEmail() {

        return new DuplicateMemberException("이미 존재하는 이메일입니다.");
    }

    public static DuplicateMemberException duplicateNickname() {

        return new DuplicateMemberException("이미 존재하는 닉네임입니다.");
    }
}
