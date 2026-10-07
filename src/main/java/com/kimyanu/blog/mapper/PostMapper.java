package com.kimyanu.blog.mapper;

import com.kimyanu.blog.dto.post.PostDto;
import com.kimyanu.blog.dto.post.PostRequest;
import com.kimyanu.blog.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Post toEntity(PostRequest postRequest);

    @Mapping(source = "member.id" , target = "authorId")
    @Mapping(source = "member.nickname", target = "authorNickname")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    PostDto toDto(Post post);

    List<PostDto> toDtoList(List<Post> posts);
}
