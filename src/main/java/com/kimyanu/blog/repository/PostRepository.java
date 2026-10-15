package com.kimyanu.blog.repository;

import com.kimyanu.blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 조회/검색: find...By, read/..By, query...By, get...By, stream...By
 * 존재여부 확인: exists...By
 * 개수 카운팅: count...By
 * 삭제: delete...By, remove...By
 */

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @EntityGraph(attributePaths = {"member","category"})
    Page<Post> findAllBy(Pageable pageDetails);

    @EntityGraph(attributePaths = {"member", "category"})
    Optional<Post> findWithMemberAndCategoryById(Long postId);

    @EntityGraph(attributePaths = {"member", "category"})
    Page<Post> findByIsVisibleTrue(Pageable pageDetails);

    @Modifying
    @Query("UPDATE Post p SET p.isVisible = false WHERE p.member.id = :memberId")
    void hideAllByMemberId(Long memberId);
}
