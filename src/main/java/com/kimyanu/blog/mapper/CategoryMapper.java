package com.kimyanu.blog.mapper;

import com.kimyanu.blog.dto.category.CategoryRequest;
import com.kimyanu.blog.dto.category.CategoryResponse;
import com.kimyanu.blog.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    // DTO -> Entity
    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryRequest categoryRequest);

    // Entity -> DTO
    List<CategoryResponse> toResponseList(List<Category> categories);

    @Mapping(target = "postCount", ignore = true)
    CategoryResponse toResponse(Category savedCategory);
}
