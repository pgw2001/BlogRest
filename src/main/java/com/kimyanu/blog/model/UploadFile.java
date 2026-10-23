package com.kimyanu.blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class UploadFile {

    public static final int MAX_SIZE = 5 * 1024 * 1024;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 36)
    private String storeName;   // 서버에 저장되는 파일 이름

    // 올린 사람의 컴퓨터에 있던 파일 이름
    @Column(nullable = false)
    private String originalName;

    // 파일의 형식(images/png, image/jped, application/pdf 등)
    @Column(nullable = false, length = 50)
    private String contentType;

    // 파일 크기
    @Column(nullable = false)
    private Long size;

    // 파일이 저장된 경로
    @Lob
    @Column(nullable = false, length = MAX_SIZE)
    private byte[] data;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member uploader;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
