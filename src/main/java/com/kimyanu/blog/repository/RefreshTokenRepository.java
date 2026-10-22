package com.kimyanu.blog.repository;

import com.kimyanu.blog.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteAllByMemberId(Long memberid);
}
