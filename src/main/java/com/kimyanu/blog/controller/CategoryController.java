package com.kimyanu.blog.controller;

import com.kimyanu.blog.model.Category;
import com.kimyanu.blog.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
    public ResponseEntity<Map<String, List<String>>> createCategory(@Valid @RequestBody Category category, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errors", errors));
        }

        try {
            categoryService.createCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).build(); //.build() - 본문이 필요하지 않다면
        }catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("errors", List.of(e.getMessage())));
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

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<Map<String, List<String>>> updateCategory(@PathVariable Long categoryId, @Valid @RequestBody Category updatedCategory, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errors", errors));
        }
        try {
            categoryService.updateCategory(categoryId, updatedCategory);
            return ResponseEntity.ok().build();
        } catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("errors",List.of(e.getMessage())));
        } catch(IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("errors",List.of(e.getMessage())));
        }
    }


}
