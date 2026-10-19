package com.kimyanu.blog.dto.post;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostVisibilityRequest {

    @NotNull(message = "게시글 공개 여부는 필수입니다.")
    private Boolean isVisible;
}
