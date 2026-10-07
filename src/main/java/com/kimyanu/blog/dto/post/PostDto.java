package com.kimyanu.blog.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
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
