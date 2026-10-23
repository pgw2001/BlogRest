package com.kimyanu.blog.repository;

import com.kimyanu.blog.enums.AuthProvider;
import com.kimyanu.blog.model.SocialAccount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {

    @EntityGraph(attributePaths = {"member"})
    Optional<SocialAccount> findByProviderAndProviderId(AuthProvider authProvider, String sub);

    @Modifying
    @Query("delete from SocialAccount s where s.member.id = :memberid")
    void deleteByMemberId(@Param("memberId") Long memberId);
}
