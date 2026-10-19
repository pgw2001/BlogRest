package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.category.CategoryRequest;
import com.kimyanu.blog.dto.category.CategoryResponse;
import com.kimyanu.blog.dto.common.PageResponse;
import com.kimyanu.blog.exceptions.custom.CategoryNotFoundException;
import com.kimyanu.blog.exceptions.custom.DuplicateCategoryException;
import com.kimyanu.blog.mapper.CategoryMapper;
import com.kimyanu.blog.model.Category;
import com.kimyanu.blog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.kimyanu.blog.util.PageableFactory.create;

// 조회방식 + 조건 + 필드
// findAll()
// findbyId() -> find(방식) + By(조건) + Id(필드)
// deleteById(1) -> delete(방식) + By(조건) + Id(필드)
// existsById(1) -> exists(방식) + By(조건) + Id(필드)
// deleteAll() -> delete(방식) + All(조건)

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private static final List<String> SORTABLE_FIELDS = List.of("id", "name");
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public PageResponse<CategoryResponse> getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Pageable pageable = create(pageNumber, pageSize, sortBy, sortOrder, SORTABLE_FIELDS);

        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        // Page<?> 인터페이스와 getContent() 메서드
        // Page<?> 인터페이스에서 제공하는 메서드로, 페이징 처리된 결과 데이터만을 List<?> 형태로 반환한다.
        List<Category> categories =  categoryPage.getContent();

        // 엔티티를 DTO로 변환하여 반환 (Entity -> DTO)
        return PageResponse.of(categoryPage, categoryMapper.toResponseList(categories));
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        boolean duplicate = categoryRepository.existsByNameIgnoreCase(categoryRequest.getName());

        if(duplicate) {
            throw new DuplicateCategoryException();
        }

        // Request DTO를 엔티티로 변환하고 저장 (DTO -> Entity)
        Category categoryEntity = categoryMapper.toEntity(categoryRequest);
        Category savedCategory = categoryRepository.save(categoryEntity);
        return categoryMapper.toResponse(savedCategory);
    }

    @Transactional
    public CategoryResponse updateCategory(Long categoryId, CategoryRequest categoryRequest) {

        Category existCategory = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        boolean duplicate = categoryRepository.existsByNameIgnoreCaseAndIdNot(categoryRequest.getName(), categoryId);

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
        return categoryMapper.toResponse(existCategory);
    }

    @Transactional
    public CategoryResponse deleteCategory(Long categoryId) {
        // 데이터를 지운다는 목적은 똑같다.
        // findById + delete() : 존재 확인 + 객체가 필요할 때.
        // existsById + deleteById : 존재 여부를 명시적으로 확인하고 삭제할 때.
        // deleteById : 존재 여부를 굳이 확인하지 않고 바로 삭제할 때.
        // findById + 예외 + delete() : 존재 여부를 확인하고, 존재하지 않으면 예외 처리까지 하면서 삭제할 때.
        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);

        categoryRepository.delete(category);
//         categoryRepository.deleteById(category);
        return categoryMapper.toResponse(category);
    }
}
