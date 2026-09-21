package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.CategoryDto;
import com.kimyanu.blog.dto.CategoryRequest;
import com.kimyanu.blog.dto.CategoryResponse;
import com.kimyanu.blog.exceptions.CategoryNotFoundException;
import com.kimyanu.blog.model.Category;
import com.kimyanu.blog.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// 조회방식 + 조건 + 필드
// findAll()
// findbyId() -> find(방식) + By(조건) + Id(필드)
// deleteById(1) -> delete(방식) + By(조건) + Id(필드)
// existsById(1) -> exists(방식) + By(조건) + Id(필드)
// deleteAll() -> delete(방식) + All(조건)

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponse getAllCategories() {
        List<Category> categories =  categoryRepository.findAll();

        // 엔티티를 DTO로 변환하여 반환 (Entity -> DTO)
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categories.stream()
                .map(category -> new CategoryDto(category.getId(), category.getName()))
                .toList());

        return categoryResponse;
    }

    @Override
    public void createCategory(CategoryRequest categoryRequest) {
        boolean duplicate = categoryRepository.existsByName(categoryRequest.getName());

        if(duplicate) {
            throw new IllegalStateException("이미 존재하는 카테고리입니다: " + categoryRequest.getName());
        }

        // Request DTO를 엔티티로 변환하고 저장
        Category categoryEntity = new Category();
        categoryEntity.setName(categoryRequest.getName());
        categoryRepository.save(categoryEntity);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        // 데이터를 지운다는 목적은 똑같다.
        // findById + delete() : 존재 확인 + 객체가 필요할 때.
        // existsById + deleteById : 존재 여부를 명시적으로 확인하고 삭제할 때.
        // deleteById : 존재 여부를 굳이 확인하지 않고 바로 삭제할 때.
        // findById + 예외 + delete() : 존재 여부를 확인하고, 존재하지 않으면 예외 처리까지 하면서 삭제할 때.
        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        categoryRepository.delete(category);
        // categoryRepository.deleteById(category);
    }

    @Override
    public void updateCategory(Long categoryId, CategoryRequest categoryRequest) {

       Category existCategory = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        boolean duplicate = categoryRepository.existsByNameAndIdNot(categoryRequest.getName(), categoryId);

        /*
        boolean nameChanged = !existCategory.getName().equals(categoryRequest.getName());
        if (nameChanged && duplicate) {
            throw new IllegalStateException("이미 존재하는 카테고리입니다: " + categoryRequest.getName());
        }
         */

        if(duplicate) {
            throw new IllegalStateException("이미 존재하는 카테고리입니다: " + categoryRequest.getName());
        }

        existCategory.setName(categoryRequest.getName());
        categoryRepository.save(existCategory);

    }

}
