package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.comment.CommentDto;
import com.kimyanu.blog.dto.comment.CommentRequest;
import com.kimyanu.blog.dto.comment.CommentResponse;

public interface CommentService {
    CommentResponse getCommentsByPostId(
            Long postId,
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortOrder);

    CommentDto createComment(Long memberId, Long postId, CommentRequest commentRequest);

    CommentDto updateComment(Long memberId, Long commentId, CommentRequest commentRequest);

    void deleteComment(Long memberId, Long commentId);
}
