package com.kimyanu.blog.dto.auth;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonPropertyOrder({"accessToken", "tokenType"})
public class LoginResponse {
    private String accessToken;
    private String tokenType;
}
