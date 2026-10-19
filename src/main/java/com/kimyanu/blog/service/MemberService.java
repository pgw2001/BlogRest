package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.common.PageResponse;
import com.kimyanu.blog.dto.member.MemberDto;
import com.kimyanu.blog.dto.member.MemberPublicDto;
import com.kimyanu.blog.dto.member.MemberRequest;
import com.kimyanu.blog.dto.member.MemberUpdateRequest;
import com.kimyanu.blog.enums.UserRole;
import com.kimyanu.blog.exceptions.custom.DuplicateMemberException;
import com.kimyanu.blog.exceptions.custom.MemberNotFoundException;
import com.kimyanu.blog.exceptions.custom.ReservedMemberValueException;
import com.kimyanu.blog.mapper.MemberMapper;
import com.kimyanu.blog.model.Member;
import com.kimyanu.blog.repository.MemberRepository;
import com.kimyanu.blog.repository.PostRepository;
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

    @Transactional(readOnly = true)
    public PageResponse<MemberDto> getAllMembers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {


        Pageable pageable = create(pageNumber, pageSize, sortBy, sortOrder, SORTABLE_FIELDS);

        Page<Member> memberPage = memberRepository.findAllByDeletedAtNull(pageable);

        List<Member> members = memberPage.getContent();


        return PageResponse.of(memberPage, memberMapper.toDtoList(members));
    }

    public MemberPublicDto getMemberById(Long memberId) {

        Member member = findExistingMember(memberId);

        return memberMapper.toPublicDto(member);
    }

    @Transactional
    public void createMember(MemberRequest memberRequest) {

        ensureNotReservedNickname(memberRequest.getNickname());
        ensureNotReservedEmail(memberRequest.getEmail());

        if(memberRepository.existsByEmail(memberRequest.getEmail())) {
            throw DuplicateMemberException.duplicateEmail();
        }

        if(memberRepository.existsByNicknameIgnoreCase(memberRequest.getNickname())) {
            throw DuplicateMemberException.duplicateNickname();
        }

        Member memberEntity = memberMapper.toEntity(memberRequest);
        memberEntity.setRole(UserRole.USER);
        memberEntity.setPassword(passwordEncoder.encode(memberRequest.getPassword()));
        memberRepository.save(memberEntity);
    }

    @Transactional
    public MemberPublicDto updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest) {
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
        return memberMapper.toPublicDto(existingMember);
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
