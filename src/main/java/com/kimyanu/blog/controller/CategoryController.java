package com.kimyanu.blog.controller;

import com.kimyanu.blog.config.AppConstant;
import com.kimyanu.blog.dto.CategoryRequest;
import com.kimyanu.blog.dto.CategoryResponse;
import com.kimyanu.blog.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public CategoryResponse getCategories(
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.SORT_CATEGORY_BY) String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = AppConstant.SORT_CATEGORY_ASC) String sortOrder
    ) {
        System.out.println(sortBy + ", "  + sortOrder);
        return categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<Void> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
            categoryService.createCategory(categoryRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build(); //.build() - 본문이 필요하지 않다면
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<Void> removeCategory(@PathVariable Long categoryId) {
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long categoryId, @Valid @RequestBody CategoryRequest updatedCategoryRequest) {
        categoryService.updateCategory(categoryId, updatedCategoryRequest);
        return ResponseEntity.ok().build();
    }
}
