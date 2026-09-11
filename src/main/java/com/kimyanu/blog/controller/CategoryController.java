package com.kimyanu.blog.controller;

import com.kimyanu.blog.model.Category;
import com.kimyanu.blog.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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
    public ResponseEntity<String> createCategory(@RequestBody Category category) {
        try {
            categoryService.createCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body("Created category successfully!"); //.build() - 본문이 필요하지 않다면
        }catch (IllegalStateException e) {
            return  ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> removeCategory(@PathVariable Long categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

//    @PutMapping("/admin/categories/{categoryId}")
//    public ResponseEntity<String> updateCategory(@PathVariable Long categoryId) {
//        try {
//
//        } catch() {
//
//        }
//    }


}
