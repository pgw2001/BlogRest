package com.kimyanu.blog.dto.post;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String thumbnail;
    private Boolean isVisible;
    private LocalDateTime createdAt;

    private Long authorId;
    private String authorNickname;
    private Long categoryId;
    private String categoryName;
}
