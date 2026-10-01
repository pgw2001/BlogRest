package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.member.MemberRequest;
import com.kimyanu.blog.dto.member.MemberResponse;

public interface MemberService {
    void createMember(MemberRequest memberRequest);

    MemberResponse getAllMembers();
}
