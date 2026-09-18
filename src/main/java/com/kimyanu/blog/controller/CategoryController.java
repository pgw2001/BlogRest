package com.kimyanu.blog.controller;

import com.kimyanu.blog.model.Category;
import com.kimyanu.blog.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Void> createCategory(@Valid @RequestBody Category category) {
            categoryService.createCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).build(); //.build() - 본문이 필요하지 않다면
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<Void> removeCategory(@PathVariable Long categoryId) {
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long categoryId, @Valid @RequestBody Category updatedCategory) {
        categoryService.updateCategory(categoryId, updatedCategory);
        return ResponseEntity.ok().build();
    }
}
