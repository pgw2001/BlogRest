package com.kimyanu.blog.service;

import com.kimyanu.blog.exceptions.custom.DuplicateLikesException;
import com.kimyanu.blog.exceptions.custom.LikesNotFoundException;
import com.kimyanu.blog.exceptions.custom.MemberNotFoundException;
import com.kimyanu.blog.exceptions.custom.PostNotFoundException;
import com.kimyanu.blog.model.Likes;
import com.kimyanu.blog.model.Member;
import com.kimyanu.blog.model.Post;
import com.kimyanu.blog.repository.LikesRepository;
import com.kimyanu.blog.repository.MemberRepository;
import com.kimyanu.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikesService{

    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public void createLike(Long memberId, Long postId) {


        Post post = postRepository.findByIdAndIsVisibleTrue(postId)
                .orElseThrow(PostNotFoundException::new);

        Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(MemberNotFoundException::new);

        if(likesRepository.existsByPostAndMember(post, member)) {
            throw new DuplicateLikesException();
        }

        Likes likes = new Likes();
        likes.setMember(member);
        likes.setPost(post);

        likesRepository.save(likes);
    }

    @Transactional
    public void deleteLike(Long memberId, Long postId) {

        Likes likes = likesRepository.findByPostIdAndMemberId(postId, memberId)
                        .orElseThrow(LikesNotFoundException::new);

        likesRepository.delete(likes);
    }
}
