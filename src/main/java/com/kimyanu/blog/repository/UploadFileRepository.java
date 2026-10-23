package com.kimyanu.blog.repository;

import com.kimyanu.blog.model.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {

    Optional<UploadFile> findByStoreName(String storedName);
}
