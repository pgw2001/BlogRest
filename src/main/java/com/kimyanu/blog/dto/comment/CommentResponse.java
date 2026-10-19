package com.kimyanu.blog.dto.comment;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonPropertyOrder({"id", "content", "authorNickname", "createAt"})
public class CommentResponse {
    private Long id;
    private String content;
    private String authorNickname;
    private LocalDateTime createdAt;
}
