package com.kimyanu.blog.mapper;

import com.kimyanu.blog.dto.comment.CommentRequest;
import com.kimyanu.blog.dto.comment.CommentResponse;
import com.kimyanu.blog.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Comment toEntity(CommentRequest commentRequest);

    @Mapping(target = "authorNickname", source = "member.nickname")
    @Mapping(target = "authorId", source = "member.id")
    CommentResponse toResponse(Comment comment);

    List<CommentResponse> toResponseList(List<Comment> comments);
}
