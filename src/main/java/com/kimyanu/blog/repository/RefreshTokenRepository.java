package com.kimyanu.blog.repository;

import com.kimyanu.blog.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteAllByMemberId(Long memberid);

    Optional<RefreshToken> findByToken(String refreshToken);

    @Modifying
    @Query("delete from RefreshToken  r where r.token = :token")
    void deleteByToken(@Param("token") String refreshToken);

    @Modifying
    @Query("delete from RefreshToken r where r.expiresAt < :now")
    int deleteExpired(@Param("now")LocalDateTime now);
}
