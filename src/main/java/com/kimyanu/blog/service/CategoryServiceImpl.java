package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.CategoryRequest;
import com.kimyanu.blog.dto.CategoryResponse;
import com.kimyanu.blog.exceptions.CategoryNotFoundException;
import com.kimyanu.blog.mapper.CategoryMapper;
import com.kimyanu.blog.model.Category;
import com.kimyanu.blog.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        // Sort 객체
        // 스프링 데이터 JPA에서 제공하는 객체로, 정렬 기준과 방향을 설정할 수 있다.
        Sort sort = Sort.by(
                sortOrder.equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC,
                sortBy
        );

        // Pageable 클래스
        // 스프링 데이터 JPA에서 제공되는 인터페이스로, 페이징과 정렬 정보를 객체화하여 다루기 위해 제공되는 인터페이스다.
        Pageable pageDetails = PageRequest.of(pageNumber - 1, pageSize, sort);

        // Page<?> 인터페이스
        // 스프링 데이터 JPA에서 제공하는 인터페이스로, 페이징 처리된 결과를 담는 객체다.
        // 일반적인 List<?>와 달리, 페이지 정보(총 페이지 수, 현재 페이지 번호, 페이지 크기 등)와 함께 결과 데이터를 제공한다.
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        // Page<?> 인터페이스와 getContent() 메서드
        // Page<?> 인터페이스에서 제공하는 메서드로, 페이징 처리된 결과 데이터만을 List<?> 형태로 반환한다.
        List<Category> categories =  categoryPage.getContent();

        // 엔티티를 DTO로 변환하여 반환 (Entity -> DTO)
        return new CategoryResponse(
                categoryMapper.toResponseList(categories),
                categoryPage.getNumber() + 1,
                categoryPage.getSize(),
                categoryPage.getTotalElements(),
                categoryPage.getTotalPages(),
                categoryPage.isLast()
        );
    }

    @Override
    public void createCategory(CategoryRequest categoryRequest) {
        boolean duplicate = categoryRepository.existsByName(categoryRequest.getName());

        if(duplicate) {
            throw new IllegalStateException("이미 존재하는 카테고리입니다: " + categoryRequest.getName());
        }

        // Request DTO를 엔티티로 변환하고 저장 (DTO -> Entity)
        Category categoryEntity = categoryMapper.toEntity(categoryRequest);
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
