package com.kimyanu.blog.service;

import com.kimyanu.blog.model.Likes;
import com.kimyanu.blog.model.Member;
import com.kimyanu.blog.model.Post;
import com.kimyanu.blog.repository.LikesRepository;
import com.kimyanu.blog.repository.MemberRepository;
import com.kimyanu.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikesServiceImpl implements LikesService{

    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public void createLike(Long memberId, Long postId) {


        Post post = postRepository.findByIdAndIsVisibleTrue(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다." + postId));

        Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다. " + memberId));

        if(likesRepository.existsByPostAndMember(post, member)) {
            throw new IllegalArgumentException("이미 좋아요를 누른 게시글입니다: " + postId);
        }

        Likes likes = new Likes();
        likes.setMember(member);
        likes.setPost(post);

        likesRepository.save(likes);
    }

    @Override
    @Transactional
    public void deleteLike(Long memberId, Long postId) {

        Likes likes = likesRepository.findByPostIdAndMemberId(postId, memberId)
                        .orElseThrow(() -> new NoSuchElementException("좋아요가 존재하지 않습니다: " + postId));

        likesRepository.delete(likes);
    }
}
