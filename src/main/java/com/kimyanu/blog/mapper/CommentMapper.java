package com.kimyanu.blog.mapper;

import com.kimyanu.blog.dto.comment.CommentRequest;
import com.kimyanu.blog.dto.comment.CommentResponse;
import com.kimyanu.blog.model.Comment;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "member.nickname", target = "authorNickname")
    CommentResponse toDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Comment toEntity(CommentRequest commentRequest);

    List<CommentResponse> toDtoList(List<Comment> comments);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(@MappingTarget Comment existingComment, CommentRequest commentRequest);
}
