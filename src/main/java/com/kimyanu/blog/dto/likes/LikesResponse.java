package com.kimyanu.blog.dto.likes;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"likesCount", "liked"})
public class LikesResponse {
    private Long likesCount;
    private Boolean liked;
}
