package com.kimyanu.blog.controller;

import com.kimyanu.blog.model.Category;
import com.kimyanu.blog.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public List<Category> getCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/admin/categories")
    public String createCategory(@RequestBody Category category) {
        categoryService.createCategory(category);
        return "Category created successfully!";
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public String removeCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return "Category removed successfully!";
    }


}
