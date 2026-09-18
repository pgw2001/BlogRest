package com.kimyanu.blog.service;

import com.kimyanu.blog.model.Category;
import com.kimyanu.blog.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        // 첫 번째 방법
//        List<Category> categories = getAllCategories();
//        boolean duplicate = categories.stream()
//                .anyMatch(c -> c.getName().equals(category.getName()));
//        if (duplicate) {
//            throw new IllegalStateException("이미 존재하는 카테고리입니다: " + category.getName());
//        }

        // 두 번째 방법
        // SELECT * FROM category WHERE  name = 'category.getName()'
//        Optional<Category> existCategory = categoryRepository.findByName(category.getName());
//        if(existCategory.isPresent()) {
//            throw new IllegalStateException("이미 존재하는 카테고리입니다: " + category.getName());
//        }

        // 세 번째 방법 (성능적으로 가장 좋음)
        boolean duplicate = categoryRepository.existsByName(category.getName());

        if(duplicate) {
            throw new IllegalStateException("이미 존재하는 카테고리입니다: " + category.getName());
        }

//        if(category.getName().trim().isBlank()) {
//            throw new IllegalStateException("카테고리 이름은 필수입니다.");
//        }
        categoryRepository.save(category);
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
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 카테고리입니다: " + categoryId));

        categoryRepository.delete(category);
        // categoryRepository.deleteById(category);
    }

    @Override
    public void updateCategory(Long categoryId, Category category) {

       Category existCategory = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 카테고리입니다: " + categoryId));

//       // 이미 존재하는 카테고리 이름으로 변경하지 못하는 예외처리
//        boolean nameChanged = !existCategory.getName().equals(category.getName());
//        boolean duplicate = categoryRepository.existsByName(category.getName());
//        if(duplicate && nameChanged) {
//            throw new IllegalStateException("이미 존재하는 카테고리입니다: " + category.getName());
//        }
        boolean duplicate = categoryRepository.existsByNameAndIdNot(category.getName(), categoryId);

        if(duplicate) {
            throw new IllegalStateException("이미 존재하는 카테고리입니다: " + category.getName());
        }

        existCategory.setName(category.getName());
        categoryRepository.save(existCategory);

    }

}
