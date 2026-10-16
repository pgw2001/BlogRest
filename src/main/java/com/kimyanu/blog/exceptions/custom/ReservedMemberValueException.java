package com.kimyanu.blog.exceptions.custom;

public final class ReservedMemberValueException extends RuntimeException {
    private ReservedMemberValueException(String message) {

        super(message);
    }

    public static ReservedMemberValueException invalidEmail() {

        return new ReservedMemberValueException("허용되지 않은 이메일 도메인 형식입니다.");
    }

    public static ReservedMemberValueException invalidNickname() {

        return new ReservedMemberValueException("허용되지 않은 닉네임 형식입니다.");
    }
}
