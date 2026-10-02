package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.member.MemberPublicDto;
import com.kimyanu.blog.dto.member.MemberRequest;
import com.kimyanu.blog.dto.member.MemberResponse;
import com.kimyanu.blog.dto.member.MemberUpdateRequest;
import com.kimyanu.blog.enums.UserRole;
import com.kimyanu.blog.mapper.MemberMapper;
import com.kimyanu.blog.model.Member;
import com.kimyanu.blog.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static com.kimyanu.blog.util.PageableFactory.getPageable;

@Service
public class MemberServiceImpl implements MemberService{

    private final List<String> SORTABLE_FIELDS = List.of("id", "nickname", "email", "createdAt");
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public MemberServiceImpl(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    @Override
    public MemberResponse getAllMembers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {


        Pageable pageable = getPageable(pageNumber, pageSize, sortBy, sortOrder, SORTABLE_FIELDS);

        Page<Member> memberPage = memberRepository.findAll(pageable);

        List<Member> members = memberPage.getContent();


        return new MemberResponse(
                memberMapper.toDtoList(members),
                memberPage.getNumber() + 1,
                memberPage.getSize(),
                memberPage.getTotalElements(),
                memberPage.getTotalPages(),
                memberPage.isLast()
        );
    }

    @Override
    public MemberPublicDto getMemberById(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 id의 회원을 찾을 수 없습니다. : " + memberId));

        return memberMapper.toPublicDto(member);
    }

    @Override
    @Transactional
    public void createMember(MemberRequest memberRequest) {
        if(memberRepository.existsByEmail(memberRequest.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + memberRequest.getEmail());
        }

        if(memberRepository.existsByNickname(memberRequest.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + memberRequest.getNickname());
        }

        Member memberEntity = memberMapper.toEntity(memberRequest);
        memberEntity.setRole(UserRole.USER);
        memberRepository.save(memberEntity);
    }

    @Override
    @Transactional
    public MemberPublicDto updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest) {
        // 가져오기
        Member existingMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 id의 회원을 찾을 수 없습니다. : " + memberId));

        // 예외처리
        boolean nicknameChanged = !existingMember.getNickname().equals(memberUpdateRequest.getNickname());
        if (nicknameChanged && memberRepository.existsByNickname(memberUpdateRequest.getNickname())) {
            throw new IllegalStateException("이미 사용 중인 닉네임입니다: " + memberUpdateRequest.getNickname());
        }

        memberMapper.updateEntityFromRequest(existingMember, memberUpdateRequest);
        return memberMapper.toPublicDto(existingMember);
    }

}
