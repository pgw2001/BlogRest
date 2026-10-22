package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.profile.ProfileRequest;
import com.kimyanu.blog.dto.profile.ProfileResponse;
import com.kimyanu.blog.exceptions.custom.MemberNotFoundException;
import com.kimyanu.blog.mapper.ProfileMapper;
import com.kimyanu.blog.model.Member;
import com.kimyanu.blog.model.Profile;
import com.kimyanu.blog.repository.MemberRepository;
import com.kimyanu.blog.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final MemberRepository memberRepository;

    public ProfileResponse getProfile(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        return profileRepository.findByMemberId(memberId)
                .map(profileMapper::toResponse)
                .orElseGet(() -> profileMapper.toEmptyResponse(member));
    }

    @Transactional
    public ProfileResponse updateProfile(Long memberId, ProfileRequest profileRequest) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        // 프로필은 가입할 때 만들지 않으므로, 처음 수정할 때 만든다
        Profile profile = profileRepository.findByMemberId(memberId)
                .orElseGet(() -> {
                    Profile newProfile = new Profile();
                    newProfile.setMember(member);
                    return newProfile;
                });

        profileMapper.updateEntityFromRequest(profile, profileRequest);
        Profile savedProfile = profileRepository.save(profile);

        return profileMapper.toResponse(savedProfile);
    }
}
