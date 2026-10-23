package com.kimyanu.blog.exceptions.custom;

public class UploadedFileNotFoundException extends RuntimeException {
    public UploadedFileNotFoundException() {

        super("요청한 경로의 파일을 찾을 수 없습니다.");
    }
}
