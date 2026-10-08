package com.kimyanu.blog.repository;

import com.kimyanu.blog.model.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @EntityGraph(attributePaths = {"member","category"})
    List<Post> findAllBy();

    @EntityGraph(attributePaths = {"member", "category"})
    Optional<Post> findWithMemberAndCategoryById(Long postId);
}
