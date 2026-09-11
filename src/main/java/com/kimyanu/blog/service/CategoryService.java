package com.kimyanu.blog.service;

import com.kimyanu.blog.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    void createCategory(Category category);

    void deleteCategory(Long categoryId);

    void updateCategory(Long id, Category category);
}
