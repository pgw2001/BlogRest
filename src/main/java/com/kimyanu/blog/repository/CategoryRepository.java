package com.kimyanu.blog.repository;

import com.kimyanu.blog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long categoryId);

    @Query(value = "SELECT * FROM category LIMIT :pageSize OFFSET :offset", nativeQuery = true)
    List<Category> findAllByNativeQuery(@Param("pageSize") Integer pageSize, @Param("offset") Integer offset);
}
