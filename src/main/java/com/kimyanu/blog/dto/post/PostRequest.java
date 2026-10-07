package com.kimyanu.blog.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    @NotBlank(message = "제목은 필수입니다.")
    @Size(min = 2, max = 100, message = "제목은 2자 이상 100자 이하로 작성해주세요.")
    private String title;

    @NotBlank(message = "본문은 필수입니다.")
    private String content;

    private String thumbnail;

    @NotNull(message = "카테고리는 필수입니다.")
    private Long categoryId;

    private Boolean isVisible;
    // memberId 없음 — 서버가 정한다
}
