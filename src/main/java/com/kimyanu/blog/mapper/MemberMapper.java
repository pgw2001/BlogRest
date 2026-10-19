package com.kimyanu.blog.mapper;

import com.kimyanu.blog.dto.member.MemberCreateRequest;
import com.kimyanu.blog.dto.member.MemberPublicResponse;
import com.kimyanu.blog.dto.member.MemberResponse;
import com.kimyanu.blog.model.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Member toEntity(MemberCreateRequest memberCreateRequest);

    MemberResponse toResponse(Member savedMember);

    MemberPublicResponse toPublicResponse(Member savedMember);

    List<MemberResponse> toResponseList(List<Member> savedMemberList);
}
