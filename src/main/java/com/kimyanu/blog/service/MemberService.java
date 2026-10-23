package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.auth.SocialAccountResponse;
import com.kimyanu.blog.dto.common.PageResponse;
import com.kimyanu.blog.dto.member.*;
import com.kimyanu.blog.dto.profile.ProfileResponse;
import com.kimyanu.blog.enums.MemberRole;
import com.kimyanu.blog.exceptions.custom.DuplicateMemberException;
import com.kimyanu.blog.exceptions.custom.MemberNotFoundException;
import com.kimyanu.blog.exceptions.custom.ReservedMemberValueException;
import com.kimyanu.blog.mapper.MemberMapper;
import com.kimyanu.blog.mapper.ProfileMapper;
import com.kimyanu.blog.model.Member;
import com.kimyanu.blog.model.SocialAccount;
import com.kimyanu.blog.repository.MemberRepository;
import com.kimyanu.blog.repository.PostRepository;
import com.kimyanu.blog.repository.ProfileRepository;
import com.kimyanu.blog.repository.SocialAccountRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.kimyanu.blog.util.PageableFactory.create;

@Service
@RequiredArgsConstructor
public class MemberService{

    private static final List<String> SORTABLE_FIELDS = List.of("id", "nickname", "email", "createdAt");
    private static final String DELETED_NICKNAME_SUFFIX = "탈퇴한회원_";
    private static final String DELETED_EMAIL_SUFFIX = "deleted_";
    private static final String DELETED_EMAIL_DOMAIN = "@deleted.local";

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final SocialAccountRepository socialAccountRepository;
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @Transactional(readOnly = true)
    public PageResponse<MemberResponse> getAllMembers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {


        Pageable pageable = create(pageNumber, pageSize, sortBy, sortOrder, SORTABLE_FIELDS);

        Page<Member> memberPage = memberRepository.findAllByDeletedAtNull(pageable);

        List<Member> members = memberPage.getContent();


        List<MemberResponse> responses = memberMapper.toResponseList(members);
        // ponytail: 회원마다 count 쿼리 1번(페이지당 최대 100번). 느려지면 group by 한 번으로 묶는다
        responses.forEach(r -> r.setPostCount(postRepository.countByMemberId(r.getId())));
        return PageResponse.of(memberPage, responses);
    }

    public MemberPublicResponse getMemberById(Long memberId) {

        Member member = findExistingMember(memberId);

        return memberMapper.toPublicResponse(member);
    }

    @Transactional(readOnly = true)
    public MemberMeResponse getCurrentMember(Long memberId) {

        Member member = findExistingMember(memberId);

        // 프로필은 가입할 때 만들지 않으므로, 처음 조회할 때 없으면 빈 프로필을 만들어서 반환한다.
        ProfileResponse profileResponse = profileRepository.findByMemberId(memberId)
                .map(profileMapper::toResponse)
                .orElseGet(() -> profileMapper.toEmptyResponse(member));

        // 소셜 계정은 가입할 때 만들므로, 없으면 빈 리스트를 반환한다.
        List<SocialAccount> socialAccounts = socialAccountRepository.findByMemberIdOrderByIdAsc(memberId);
        List<SocialAccountResponse> socialAccountResponses = memberMapper.toSocialAccountResponseList(socialAccounts);

        return memberMapper.toMeResponse(member, profileResponse, socialAccountResponses);
    }

    @Transactional
    public Member createMember(MemberCreateRequest memberCreateRequest) {

        ensureNotReservedNickname(memberCreateRequest.getNickname());
        ensureNotReservedEmail(memberCreateRequest.getEmail());

        if(memberRepository.existsByEmail(memberCreateRequest.getEmail())) {
            throw DuplicateMemberException.duplicateEmail();
        }

        if(memberRepository.existsByNicknameIgnoreCase(memberCreateRequest.getNickname())) {
            throw DuplicateMemberException.duplicateNickname();
        }

        Member memberEntity = memberMapper.toEntity(memberCreateRequest);
        memberEntity.setRole(MemberRole.USER);
        memberEntity.setPassword(passwordEncoder.encode(memberCreateRequest.getPassword()));

        return memberRepository.save(memberEntity);
    }

    @Transactional
    public MemberPublicResponse updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest) {
        // 가져오기
        Member existingMember = findExistingMember(memberId);

        // 닉네임 변경 사항 처리 (null이 아닌 경우에만 업데이트)
        String newNickname = memberUpdateRequest.getNickname();

        if (newNickname != null && !newNickname.isBlank()) {
            ensureNotReservedNickname(newNickname);
            boolean nicknameChanged = !existingMember.getNickname().equalsIgnoreCase(newNickname);

            if (nicknameChanged && memberRepository.existsByNicknameIgnoreCase(newNickname)) {
                throw DuplicateMemberException.duplicateNickname();
            }
            existingMember.setNickname(newNickname);
        }

        // 비밀번호 변경 사항 처리
        if (memberUpdateRequest.getPassword() != null) {
            existingMember.setPassword(passwordEncoder.encode(memberUpdateRequest.getPassword()));
        }
        return memberMapper.toPublicResponse(existingMember);
    }

    @Transactional
    public MemberAdminResponse setRole(Long adminId, Long memberId, MemberRoleUpdateRequest roleUpdateRequest) {

        Member member = findExistingMember(memberId);

        if(member.getId().equals(adminId)) {
            throw new IllegalArgumentException("관리자는 자신의 역할을 변경할 수 없습니다.");
        }

        member.setRole(roleUpdateRequest.getRole());
        return memberMapper.toAdminResponse(member);
    }

    @Transactional
    public void deleteMember(Long memberId) {

        // 1. 회원 존재 여부 확인 및 정보 저장
        Member member = findExistingMember(memberId);

        // 2. 탈퇴 일자 설정
        member.setDeletedAt(LocalDateTime.now());

        // 닉네임은 개인정보는 아니지만 사이트 정책에 의거하여 남기지 않는다.
        member.setNickname(DELETED_NICKNAME_SUFFIX + memberId);

        // 이메일은 개인정보이므로 즉시 파기한다.
        member.setEmail(DELETED_EMAIL_SUFFIX + memberId + DELETED_EMAIL_DOMAIN);


        // 게시글은 삭제하지는 않지만, 모두 비공개 처리한다.
        postRepository.hideAllByMemberId(memberId);

        socialAccountRepository.deleteByMemberId(memberId);

        member.setPassword(null);
    }

    private @NonNull Member findExistingMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    private static void ensureNotReservedNickname(String nickname) {
        if(nickname.startsWith(DELETED_NICKNAME_SUFFIX)) {
            throw ReservedMemberValueException.invalidNickname();
        }
    }

    private static void ensureNotReservedEmail(String email) {
        if(email.endsWith(DELETED_EMAIL_DOMAIN)) {
            throw ReservedMemberValueException.invalidEmail();
        }
    }
}
