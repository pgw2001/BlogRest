package com.kimyanu.blog.controller;

import com.kimyanu.blog.dto.file.FileContent;
import com.kimyanu.blog.dto.file.FileUploadResponse;
import com.kimyanu.blog.security.CustomUserDetails;
import com.kimyanu.blog.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/public/files/{storedName}")
    public ResponseEntity<byte[]> getFile(@PathVariable String storedName) {
        FileContent file = fileService.getFile(storedName);

        // 같은 주소의 이미지는 바뀌지 않으므로(새로 올리면 새 주소) 브라우저가 1년 동안 다시 받지 않게 한다
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.contentType()))
                .cacheControl(CacheControl.maxAge(Duration.ofDays(365)).cachePublic().immutable())
                .body(file.data());
    }

    @PostMapping(value = "/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponse> uploadFIle(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("file") MultipartFile file
            ) throws IOException {
        String storedName = fileService.upload(userDetails.getMemberId(), file);

        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(FileService.FILE_PATH + "/" + storedName)
                .toUriString();

        return ResponseEntity
                .created(URI.create(url))
                .body(new FileUploadResponse(url));
    }
}
