package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.file.FileContent;
import com.kimyanu.blog.exceptions.custom.InvalidFileException;
import com.kimyanu.blog.exceptions.custom.MemberNotFoundException;
import com.kimyanu.blog.exceptions.custom.UploadedFileNotFoundException;
import com.kimyanu.blog.model.Member;
import com.kimyanu.blog.model.UploadFile;
import com.kimyanu.blog.repository.MemberRepository;
import com.kimyanu.blog.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {
    // 이미지를 보는 주소의 경로
    public static final String FILE_PATH = "/api/public/files/";
    private static final String DEFAULT_NAME = "image";
    private static final int MAX_NAME_LENGTH = 255;

    private final UploadFileRepository uploadFileRepository;
    private final MemberRepository memberRepository;

    private static String detectImageType(byte[] data) {
        // 앞 12바이트를 글자로 바꿔서 startsWith로 비교한다. ISO_8859_1은 바이트 하나를 글자 하나로 그대로 바꾼다
        String head = new String(data, 0, Math.min(data.length, 12), StandardCharsets.ISO_8859_1);
        if (head.startsWith("\u0089PNG\r\n\u001A\n")) {
            return "image/png";
        }
        if (head.startsWith("ÿØÿ")) {
            return "image/jpeg";
        }
        if (head.startsWith("GIF87a") || head.startsWith("GIF89a")) {
            return "image/gif";
        }
        // WebP는 RIFF로 시작하고, 8번째 바이트부터 WEBP가 온다. 사이의 4바이트는 파일 크기다
        if (head.startsWith("RIFF") && head.startsWith("WEBP", 8)) {
            return "image/webp";
        }
        return null;
    }

    @Transactional
    public String upload(Long memberId, MultipartFile file) throws IOException {

        Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(MemberNotFoundException::new);

        if(file.isEmpty()) {
            throw InvalidFileException.empty();
        }

        if (file.getSize() > 5 * 1024 * 1024) { // 5MB
            throw InvalidFileException.toolLarge();
        }

        byte[] data = file.getBytes();
        String contentType = detectImageType(data);
        if (contentType == null) {
            throw InvalidFileException.unsupportedType();
        }

        UploadFile uploadFile = new UploadFile();
        uploadFile.setStoreName(UUID.randomUUID().toString());
        uploadFile.setOriginalName(toOriginalName(file.getOriginalFilename()));
        uploadFile.setContentType(contentType);
        uploadFile.setSize((long) data.length);
        uploadFile.setUploader(member);
        uploadFile.setData(data);
        uploadFileRepository.save(uploadFile);
        return uploadFile.getStoreName();
    }

    // 브라우저에 따라서 (C:\fakepath\) 같은 경로가 붙어서 오는 경우가 있다. 그걸 제거하고, 이름이 너무 길면 잘라서 돌려준다
    private static String toOriginalName(String originalFileName) {
        if(originalFileName == null) {
            return DEFAULT_NAME;
        }

        // C:\fakepath\super-long-file-name.jpg -> super-long-file-name.jpg
        String name = originalFileName.substring(
                Math.max(originalFileName.lastIndexOf('/'), originalFileName.lastIndexOf('\\')) + 1).strip();

        if (name.isEmpty()) {
            return DEFAULT_NAME;
        }

        return name.length() > MAX_NAME_LENGTH ? name.substring(0, MAX_NAME_LENGTH) : name;
    }

    public FileContent getFile(String storedName) {
        UploadFile file = uploadFileRepository.findByStoreName(storedName)
                .orElseThrow(UploadedFileNotFoundException::new);

        return new FileContent(file.getContentType(), file.getData());
    }
}
