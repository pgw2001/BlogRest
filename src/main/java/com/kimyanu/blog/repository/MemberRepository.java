package com.kimyanu.blog.repository;

import com.kimyanu.blog.model.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(@NotBlank(message = "이메일은 필수입니다.") @Email(message = "이메일 형식이 올바르지 않습니다.") String email);

    boolean existsByNickname(@NotBlank(message = "닉네임은 필수입니다.") String nickname);

    Optional<Member> findByEmail(String email);

    Page<Member> findAllByDeletedAtNull(Pageable pageable);
}
