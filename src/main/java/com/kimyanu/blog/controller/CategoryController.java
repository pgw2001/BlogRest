package com.kimyanu.blog.controller;

import com.kimyanu.blog.model.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final List<Category> categories = new ArrayList<>();

    public CategoryController() {
        categories.add(new Category(1L, "일상"));
        categories.add(new Category(2L, "개발"));
        categories.add(new Category(3L, "게임"));
    }

    @GetMapping("/public/categories")
    public List<Category> getCategories() {
        return categories;
    }

    @PostMapping("/admin/categories")
    public Category createCategory(Category category) {
        categories.add(category);
        return category;
    }


}
