package com.kimyanu.blog.model;

import com.kimyanu.blog.enums.MemberRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    private String password;

    @Column(unique = true, nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role; // USER, ADMIN

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    // 토큰 무효화 기준 시간, 이 시간 이후에 발급된 토큰만 유효하다.
    private LocalDateTime tokenInvalidBefore;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
