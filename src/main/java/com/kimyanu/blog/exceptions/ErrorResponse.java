package com.kimyanu.blog.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorResponse(String errorMessage, Map<String, String> errors) {
    public ErrorResponse(String errorMessage) {
        this(errorMessage, Map.of());
    }
}
