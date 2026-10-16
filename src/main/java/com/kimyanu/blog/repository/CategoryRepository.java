package com.kimyanu.blog.repository;

import com.kimyanu.blog.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByNameIgnoreCase(@NotBlank(message = "카테고리 이름은 필수입니다.") @Size(min = 2, max = 10, message = "카테고리 이름은 최소 2자에서 최대 10자입니다.") String name);

    boolean existsByNameIgnoreCaseAndIdNot(@NotBlank(message = "카테고리 이름은 필수입니다.") @Size(min = 2, max = 10, message = "카테고리 이름은 최소 2자에서 최대 10자입니다.") String name, Long categoryId);

    @Query(value = "SELECT * FROM category LIMIT :pageSize OFFSET :offset", nativeQuery = true)
    List<Category> findAllByNativeQuery(@Param("pageSize") Integer pageSize, @Param("offset") Integer offset);
}
