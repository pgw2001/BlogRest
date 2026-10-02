package com.kimyanu.blog.mapper;

import com.kimyanu.blog.dto.member.MemberDto;
import com.kimyanu.blog.dto.member.MemberPublicDto;
import com.kimyanu.blog.dto.member.MemberRequest;
import com.kimyanu.blog.dto.member.MemberUpdateRequest;
import com.kimyanu.blog.model.Member;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberDto toDto(Member savedMember);

    MemberPublicDto toPublicDto(Member savedMember);

    List<MemberDto> toDtoList(List<Member> savedMemberList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Member toEntity(MemberRequest memberRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(@MappingTarget Member existingMember, MemberUpdateRequest memberUpdateRequest);
}
