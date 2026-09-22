package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.CategoryRequest;
import com.kimyanu.blog.dto.CategoryResponse;

public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    void createCategory(CategoryRequest categoryRequest);

    void deleteCategory(Long categoryId);

    void updateCategory(Long id, CategoryRequest categoryRequest);


}
