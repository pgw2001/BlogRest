package com.kimyanu.blog.controller;

import com.kimyanu.blog.config.AppConstant;
import com.kimyanu.blog.dto.category.CategoryRequest;
import com.kimyanu.blog.dto.category.CategoryResponse;
import com.kimyanu.blog.dto.common.PageResponse;
import com.kimyanu.blog.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<PageResponse<CategoryResponse>> getCategories(
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.SORT_CATEGORY_BY) String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = AppConstant.SORT_CATEGORY_ASC) String sortOrder
    ) {
        return ResponseEntity.ok(categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder));
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
            CategoryResponse createdCategory = categoryService.createCategory(categoryRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory); //.build() - 본문이 필요하지 않다면
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryResponse> removeCategory(@PathVariable Long categoryId) {
            CategoryResponse deletedCategory = categoryService.deleteCategory(categoryId);
            return ResponseEntity.ok(deletedCategory);
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long categoryId, @Valid @RequestBody CategoryRequest updatedCategoryRequest) {
        CategoryResponse updatedCategory = categoryService.updateCategory(categoryId, updatedCategoryRequest);
        return ResponseEntity.ok(updatedCategory);
    }
}
