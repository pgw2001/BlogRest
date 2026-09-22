package com.kimyanu.blog.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorResponse(String errorMessage, boolean status, List<String> errors) {
    public ErrorResponse(String errorMessage, boolean status) {
        this(errorMessage, status, null);
    }
}
