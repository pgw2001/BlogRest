package com.kimyanu.blog.repository;

import com.kimyanu.blog.model.Comment;
import com.kimyanu.blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByPost(Post post, Pageable pageable);
}
