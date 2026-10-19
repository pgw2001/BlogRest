package com.kimyanu.blog.controller;

import com.kimyanu.blog.config.AppConstant;
import com.kimyanu.blog.dto.common.PageResponse;
import com.kimyanu.blog.dto.member.MemberPublicResponse;
import com.kimyanu.blog.dto.member.MemberRequest;
import com.kimyanu.blog.dto.member.MemberResponse;
import com.kimyanu.blog.dto.member.MemberUpdateRequest;
import com.kimyanu.blog.security.CustomUserDetails;
import com.kimyanu.blog.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/admin/members")
    public ResponseEntity<PageResponse<MemberResponse>> getAllMembers(
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.SORT_MEMBER_BY) String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = AppConstant.SORT_MEMBER_ASC) String sortOrder
    ) {
        PageResponse<MemberResponse> response = memberService.getAllMembers(pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/members/{memberId}")
    public ResponseEntity<MemberPublicResponse> getMember(@PathVariable Long memberId) {
        MemberPublicResponse response = memberService.getMemberById(memberId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/public/members")
    public ResponseEntity<Void> createMember(@Valid @RequestBody MemberRequest memberRequest) {
        memberService.createMember(memberRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/members/me")
    public ResponseEntity<MemberPublicResponse> updateMember(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody MemberUpdateRequest memberUpdateRequest) {
        MemberPublicResponse updatedMember = memberService.updateMember(userDetails.getMemberId(), memberUpdateRequest);
        return ResponseEntity.ok(updatedMember);
    }

    // TODO: 물리 삭제 - 게시글/댓글 추가 후 논리 삭제로 전환
    // TODO: 물리 삭제 -> 논리 삭제로 전환, 이메일, 닉네임은 개인정보 법에 의거하여 즉시 파기해야 함
    // TODO: 회원 탈퇴 시 게시글/댓글은 남기고, 회원 정보는 삭제(닉네임, 이메일 등) 탈퇴 시점에 "탈퇴한회원_{memberId}"로 변경, 이메일은 "탈퇴한
    // TODO: 이메일은 "deleted_{memberId}.@deleted.local"로 변경, 비밀번호는 null로 변경.
    // TODO: 탈퇴한 회원은 회원 목록에서 조회되지 않도록 처리
    @DeleteMapping("/members/me")
    public ResponseEntity<Void> deleteMember(@AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.deleteMember(userDetails.getMemberId());
        return ResponseEntity.noContent().build();
    }
}
