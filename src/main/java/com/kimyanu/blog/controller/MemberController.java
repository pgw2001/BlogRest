package com.kimyanu.blog.controller;

import com.kimyanu.blog.config.AppConstant;
import com.kimyanu.blog.dto.member.MemberPublicDto;
import com.kimyanu.blog.dto.member.MemberRequest;
import com.kimyanu.blog.dto.member.MemberResponse;
import com.kimyanu.blog.dto.member.MemberUpdateRequest;
import com.kimyanu.blog.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/admin/members")
    public ResponseEntity<MemberResponse> getAllMembers(
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.SORT_MEMBER_BY) String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = AppConstant.SORT_MEMBER_ASC) String sortOrder
    ) {
        MemberResponse response = memberService.getAllMembers(pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/members/{memberId}")
    public ResponseEntity<MemberPublicDto> getMember(@PathVariable Long memberId) {
        MemberPublicDto response = memberService.getMemberById(memberId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/public/members")
    public ResponseEntity<Void> createMember(@Valid @RequestBody MemberRequest memberRequest) {
        memberService.createMember(memberRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // TODO: 스프링 시큐리티 도입 시 복인 확인 추가 (nickname, password)
    @PutMapping("/public/members/{memberId}")
    public ResponseEntity<MemberPublicDto> updateMember(@PathVariable Long memberId, @Valid @RequestBody MemberUpdateRequest memberUpdateRequest) {
        MemberPublicDto updatedMember = memberService.updateMember(memberId, memberUpdateRequest);
        return ResponseEntity.ok(updatedMember);
    }

    // TODO: 스프링 시큐리티 도입 시 복인 확인 추가
    // TODO: 물리 삭제 - 게시글/댓글 추가 후 논리 삭제로 전환
    @DeleteMapping("/public/members/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
