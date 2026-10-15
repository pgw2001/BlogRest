package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.member.MemberPublicDto;
import com.kimyanu.blog.dto.member.MemberRequest;
import com.kimyanu.blog.dto.member.MemberResponse;
import com.kimyanu.blog.dto.member.MemberUpdateRequest;

public interface MemberService {
    void createMember(MemberRequest memberRequest);

    MemberResponse getAllMembers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    MemberPublicDto getMemberById(Long memberId);

    MemberPublicDto updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest);

    void deleteMember(Long memberId);
}
