package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.member.MemberRequest;
import com.kimyanu.blog.dto.member.MemberResponse;
import com.kimyanu.blog.enums.UserRole;
import com.kimyanu.blog.mapper.MemberMapper;
import com.kimyanu.blog.model.Member;
import com.kimyanu.blog.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public MemberServiceImpl(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    @Override
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
    public MemberResponse getAllMembers() {
        List<Member> members = memberRepository.findAll();
//        List<MemberDto> allMembers = memberMapper.toDtoList(memberRepository.findAll());

        return new MemberResponse(
                memberMapper.toDtoList(members)
        );
    }

}
