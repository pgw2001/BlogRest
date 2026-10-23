package com.kimyanu.blog.dto.category;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"id", "name", "postCount"})
public class CategoryResponse {
    private Long id;
    private String name;
    private Long postCount;  // 이 카테고리의 글 수(감춘 글 포함). 0이 아니면 삭제할 수 없다
}
