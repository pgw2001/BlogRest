package com.kimyanu.blog.service;

import com.kimyanu.blog.model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
        category.setId(categoryIdx.incrementAndGet());
        categories.add(category);
    }

}
