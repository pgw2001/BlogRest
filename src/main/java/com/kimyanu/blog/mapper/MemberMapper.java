package com.kimyanu.blog.mapper;

import com.kimyanu.blog.dto.member.MemberPublicResponse;
import com.kimyanu.blog.dto.member.MemberRequest;
import com.kimyanu.blog.dto.member.MemberResponse;
import com.kimyanu.blog.model.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberResponse toDto(Member savedMember);

    MemberPublicResponse toPublicDto(Member savedMember);

    List<MemberResponse> toDtoList(List<Member> savedMemberList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Member toEntity(MemberRequest memberRequest);
}
