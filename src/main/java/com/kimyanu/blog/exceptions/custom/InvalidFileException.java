package com.kimyanu.blog.exceptions.custom;

public final class InvalidFileException extends RuntimeException {
    private InvalidFileException(String message) {
        super(message);
    }

    public  static InvalidFileException empty() {
        return new InvalidFileException("파일이 비어 있습니다.");
    }

    public  static InvalidFileException unsupportedType() {
        return new InvalidFileException("PNG, JPEG, GIF, WebP 이미지만 올릴 수 있습니다.");
    }

    public  static InvalidFileException toolLarge() {
        return new InvalidFileException("파일은 5MB 이하만 올릴 수 있습니다.");
    }
}
