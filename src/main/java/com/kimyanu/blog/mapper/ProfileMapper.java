package com.kimyanu.blog.mapper;

import com.kimyanu.blog.dto.profile.ProfileResponse;
import com.kimyanu.blog.model.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    @Mapping(target = "memberId", ignore = true)
    @Mapping(target = "nickname", ignore = true)
    ProfileResponse toResponse(Profile profile);
}
