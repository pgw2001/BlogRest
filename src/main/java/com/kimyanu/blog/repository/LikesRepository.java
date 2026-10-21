package com.kimyanu.blog.repository;

import com.kimyanu.blog.model.Likes;
import com.kimyanu.blog.model.Member;
import com.kimyanu.blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    boolean existsByPostAndMember(Post post, Member member);

    Optional<Likes> findByPostIdAndMemberId(Long postId, Long memberId);

    Long countByPostId(Long postId);

    Likes findByPostId(Long postId);

    List<Likes> findAllByPostId(Long postId);

    @Modifying
    @Query("delete from Likes l where l.post.id = :postId")
    void deleteAllByPostId(@Param("postId") Long postId);
}