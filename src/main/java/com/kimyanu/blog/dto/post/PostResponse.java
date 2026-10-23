package com.kimyanu.blog.dto.post;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonPropertyOrder({"id", "title", "content", "thumbnail", "isVisible", "createdAt", "authorId", "authorNickname", "categoryId", "categoryName", "commentCount", "likesCount"})
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String thumbnail;
    private Boolean isVisible;
    private LocalDateTime createdAt;
    private Boolean isLiked;

    private Long authorId;
    private String authorNickname;
    private Long categoryId;
    private String categoryName;

    private Long commentCount;
    private Long likesCount;
}
