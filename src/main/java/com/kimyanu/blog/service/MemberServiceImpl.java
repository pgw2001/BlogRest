package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.member.MemberPublicDto;
import com.kimyanu.blog.dto.member.MemberRequest;
import com.kimyanu.blog.dto.member.MemberResponse;
import com.kimyanu.blog.dto.member.MemberUpdateRequest;
import com.kimyanu.blog.enums.UserRole;
import com.kimyanu.blog.mapper.MemberMapper;
import com.kimyanu.blog.model.Member;
import com.kimyanu.blog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static com.kimyanu.blog.util.PageableFactory.create;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final List<String> SORTABLE_FIELDS = List.of("id", "nickname", "email", "createdAt");
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberResponse getAllMembers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {


        Pageable pageable = create(pageNumber, pageSize, sortBy, sortOrder, SORTABLE_FIELDS);

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
        memberEntity.setPassword(passwordEncoder.encode(memberRequest.getPassword()));
        memberRepository.save(memberEntity);
    }

    @Override
    @Transactional
    public MemberPublicDto updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest) {
        // 가져오기
        Member existingMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 id의 회원을 찾을 수 없습니다. : " + memberId));

        // 닉네임 변경 사항 처리 (null이 아닌 경우에만 업데이트)
        String newNickname = memberUpdateRequest.getNickname();
        if (newNickname != null && !newNickname.isBlank()) {
            boolean nicknameChanged = !existingMember.getNickname().equals(newNickname);
            if (nicknameChanged && memberRepository.existsByNickname(newNickname)) {
                throw new IllegalStateException("이미 사용 중인 닉네임입니다: " + newNickname);
            }
            existingMember.setNickname(newNickname);
        }

        // 비밀번호 변경 사항 처리
        if (memberUpdateRequest.getPassword() != null) {
            existingMember.setPassword(passwordEncoder.encode(memberUpdateRequest.getPassword()));
        }
        return memberMapper.toPublicDto(existingMember);
    }

    @Override
    public void deleteMember(Long memberId) {
        // 1. 객체 저장 후 delete로 삭제
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 회원을 잧을 수 없습니다. : " + memberId));
        memberRepository.delete(member);

        // 2. id 존재 확인 후 deleteById로 삭제
//        boolean existingMember = memberRepository.existsById(memberId);
//        if(!existingMember) {
//            throw new NoSuchElementException("해당 ID의 회원을 찾을 수 없습니다. : " + memberId);
//        }
//        memberRepository.deleteById(memberId);
    }

}
