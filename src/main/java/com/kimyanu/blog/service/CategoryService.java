package com.kimyanu.blog.service;

import com.kimyanu.blog.model.Category;

import java.util.Map;

public interface CategoryService {

    Map<String, Object> getAllCategories();

    void createCategory(Category category);

    void deleteCategory(Long categoryId);

    void updateCategory(Long id, Category category);
}
