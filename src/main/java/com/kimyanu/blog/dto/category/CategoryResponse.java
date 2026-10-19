package com.kimyanu.blog.dto.category;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"id", "name"})
public class CategoryResponse {
    private Long id;
    private String name;
}
