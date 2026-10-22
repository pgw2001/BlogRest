package com.kimyanu.blog.mapper;

import com.kimyanu.blog.dto.profile.ProfileRequest;
import com.kimyanu.blog.dto.profile.ProfileResponse;
import com.kimyanu.blog.model.Member;
import com.kimyanu.blog.model.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    @Mapping(target = "memberId", source = "member.id")
    @Mapping(target = "nickname", source = "member.nickname")
    ProfileResponse toResponse(Profile profile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "member", ignore = true)
    void updateEntityFromRequest(@MappingTarget Profile profile, ProfileRequest profileRequest);

    @Mapping(target = "memberId", source = "id")
    @Mapping(target = "bio", ignore = true)
    @Mapping(target = "profileImage", ignore = true)
    @Mapping(target = "snsLink", ignore = true)
    ProfileResponse toEmptyResponse(Member member);
}

