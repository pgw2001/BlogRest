package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.category.CategoryDto;
import com.kimyanu.blog.dto.category.CategoryRequest;
import com.kimyanu.blog.dto.category.CategoryResponse;

public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDto createCategory(CategoryRequest categoryRequest);

    CategoryDto deleteCategory(Long categoryId);

    CategoryDto updateCategory(Long id, CategoryRequest categoryRequest);


}
