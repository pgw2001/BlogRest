package com.kimyanu.blog.mapper;

import com.kimyanu.blog.dto.CategoryDto;
import com.kimyanu.blog.dto.CategoryRequest;
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
    List<CategoryDto> toResponseList(List<Category> categories);

    CategoryDto toDto(Category savedCategory);
}
