package com.kimyanu.blog.controller;

import com.kimyanu.blog.model.Category;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final List<Category> categories = new ArrayList<>();

    public CategoryController() {}

    @GetMapping("/public/categories")
    public List<Category> getCategories() {
        return categories;
    }

    // @RequestBody : 클라이언트가 전송한 JSON 데이터를 자바 객체로 변환하여 매개변수에 바인딩함
    @PostMapping("/admin/categories")
    public String createCategory(@RequestBody Category category) {
        categories.add(category);
        return "Category created successfully!";
    }


}
