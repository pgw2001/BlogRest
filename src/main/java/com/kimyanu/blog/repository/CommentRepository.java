package com.kimyanu.blog.repository;

import com.kimyanu.blog.model.Comment;
import com.kimyanu.blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByPost(Post post, Pageable pageable);

    @Modifying
    @Query("delete from Comment c where c.post.id = :postId")
    void deleteAllByPostId(@Param("postId") Long postId);

    Long countByPostId(Long postId);

    @Query("select c.post.id as postId, count(c) as total from Comment c where c.post.id in :postIds group by c.post.id")
    List<PostCount> countByPostIds(@Param("postIds") List<Long> postIds);
}
