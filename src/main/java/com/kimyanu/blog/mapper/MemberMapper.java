package com.kimyanu.blog.mapper;

import com.kimyanu.blog.dto.member.*;
import com.kimyanu.blog.model.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "tokenInvalidBefore", ignore = true)
    Member toEntity(MemberCreateRequest memberCreateRequest);

    MemberResponse toResponse(Member savedMember);

    MemberPublicResponse toPublicResponse(Member savedMember);

    List<MemberResponse> toResponseList(List<Member> savedMemberList);

    MemberMeResponse toMeResponse(Member member);

    MemberAdminResponse toAdminResponse(Member member);
}
