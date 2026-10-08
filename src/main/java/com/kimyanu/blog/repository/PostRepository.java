package com.kimyanu.blog.repository;

import com.kimyanu.blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @EntityGraph(attributePaths = {"member","category"})
    Page<Post> findAllBy(Pageable pageDetails);

    @EntityGraph(attributePaths = {"member", "category"})
    Optional<Post> findWithMemberAndCategoryById(Long postId);

    @EntityGraph(attributePaths = {"member", "category"})
    Page<Post> findByIsVisibleTrue(Pageable pageDetails);
}
