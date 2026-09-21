package com.kimyanu.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    @NotBlank(message = "카테고리 이름은 필수입니다.")
    @Size(min = 2, max = 10, message = "카테고리 이름은 최소 2자에서 최대 10자입니다.")
    private String name;
}
