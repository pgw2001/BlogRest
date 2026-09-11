package com.kimyanu.blog.service;

import com.kimyanu.blog.model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final List<Category> categories = new ArrayList<>();
    private final AtomicLong categoryIdx = new AtomicLong(0L);

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {

        boolean duplicate = categories.stream()
                        .anyMatch(c -> c.getName().equals(category.getName()));
        if (duplicate) {
            // 객체가 요청을 수행하기에 적절하지 않은 상태일 때 발생하는 예외
            throw new IllegalStateException("이미 존재하는 카테고리입니다: " + category.getName());
        }

        category.setId(categoryIdx.incrementAndGet());
        categories.add(category);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        boolean removed = categories.removeIf(category -> category.getId().equals(categoryId));

        if(!removed) {
            throw new NoSuchElementException("존재하지 않는 카테고리입니다: " + categoryId);
        }
    }

    @Override
    public void updateCategory(Long categoryId, Category updatedCategory) {
        Category existingCategory = categories.stream()
                .filter(c -> c.getId().equals(categoryId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 카테고리입니다: " + categoryId));

        boolean duplicate = categories.stream()
                .anyMatch(c -> !c.getId().equals(categoryId) && c.getName().equals(updatedCategory.getName()));

        if (duplicate) {
            throw new IllegalStateException("이미 존재하는 카테고리입니다: " + updatedCategory.getName());
        }

        existingCategory.setName(updatedCategory.getName());



    }

}
