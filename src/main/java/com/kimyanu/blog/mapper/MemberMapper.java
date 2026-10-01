package com.kimyanu.blog.mapper;

import com.kimyanu.blog.dto.member.MemberDto;
import org.mapstruct.Mapper;

import com.kimyanu.blog.model.Member;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberDto toDto(Member savedMember);

}
