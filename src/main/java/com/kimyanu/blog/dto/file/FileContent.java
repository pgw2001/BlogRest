package com.kimyanu.blog.dto.file;

// 이미지를 응답할 때 사용하는 DTO, contentType과 data를 담습니다.
public record FileContent(String contentType, byte[] data) {
}
