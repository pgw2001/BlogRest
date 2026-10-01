package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.CategoryDto;
import com.kimyanu.blog.dto.CategoryRequest;
import com.kimyanu.blog.dto.CategoryResponse;

public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDto createCategory(CategoryRequest categoryRequest);

    CategoryDto deleteCategory(Long categoryId);

    CategoryDto updateCategory(Long id, CategoryRequest categoryRequest);


}
