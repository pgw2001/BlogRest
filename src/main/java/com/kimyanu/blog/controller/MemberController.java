package com.kimyanu.blog.controller;

import com.kimyanu.blog.config.AppConstant;
import com.kimyanu.blog.dto.common.PageResponse;
import com.kimyanu.blog.dto.member.*;
import com.kimyanu.blog.security.CustomUserDetails;
import com.kimyanu.blog.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
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

    @GetMapping("/members/me")
    public ResponseEntity<MemberMeResponse> getCurrentMember(@AuthenticationPrincipal CustomUserDetails userDetails) {
        MemberMeResponse response = memberService.getCurrentMember(userDetails.getMemberId());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/public/members")
    public ResponseEntity<Void> createMember(@Valid @RequestBody MemberCreateRequest memberCreateRequest) {
        memberService.createMember(memberCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/members/me")
    public ResponseEntity<MemberPublicResponse> updateMember(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody MemberUpdateRequest memberUpdateRequest) {
        MemberPublicResponse updatedMember = memberService.updateMember(userDetails.getMemberId(), memberUpdateRequest);
        return ResponseEntity.ok(updatedMember);
    }

    @PatchMapping("/admin/members/{memberId}/role")
    public ResponseEntity<MemberAdminResponse> updateMemberRole(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long memberId,
            @Valid @RequestBody MemberRoleUpdateRequest roleUpdateRequest) {

        MemberAdminResponse updatedMember = memberService.setRole(userDetails.getMemberId(), memberId, roleUpdateRequest);
        return ResponseEntity.ok(updatedMember);
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<Void> deleteMember(@AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.deleteMember(userDetails.getMemberId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/members/{memberId}")
    public ResponseEntity<Void> deleteMemberByAdmin(@PathVariable Long memberId) {

        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
