package com.kimyanu.blog.controller;

import com.kimyanu.blog.dto.member.MemberRequest;
import com.kimyanu.blog.dto.member.MemberResponse;
import com.kimyanu.blog.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // TODO: 회원 목록 조회 기능 구현하기 (GET /api/admin/members), 회원 목록 조회 시 응답 값은 200 OK로 반환
    @GetMapping("/admin/members")
    public ResponseEntity<MemberResponse> getAllMembers() {
        MemberResponse response = memberService.getAllMembers();
        return ResponseEntity.ok(response);
    }

    // TODO: 회원가입 기능 구현하기 (POST /api/public/members), 회원가입 후 응답 값은 201 Created로 반환(빈 값)
    @PostMapping("/public/members")
    public ResponseEntity<Void> createMember(@Valid @RequestBody MemberRequest memberRequest) {
        memberService.createMember(memberRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
