package com.kimyanu.blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(length = 500)
    private String bio;

    @Column(name = "profile_image", length = 255)
    private String profileImage;

    @Column(name = "sns_link", length = 255)
    private String snsLink;
}
