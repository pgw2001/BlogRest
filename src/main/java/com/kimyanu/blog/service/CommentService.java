package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.comment.CommentRequest;
import com.kimyanu.blog.dto.comment.CommentResponse;
import com.kimyanu.blog.dto.common.PageResponse;
import com.kimyanu.blog.exceptions.custom.CommentNotFoundException;
import com.kimyanu.blog.exceptions.custom.MemberNotFoundException;
import com.kimyanu.blog.exceptions.custom.PostNotFoundException;
import com.kimyanu.blog.mapper.CommentMapper;
import com.kimyanu.blog.model.Comment;
import com.kimyanu.blog.model.Member;
import com.kimyanu.blog.model.Post;
import com.kimyanu.blog.repository.CommentRepository;
import com.kimyanu.blog.repository.MemberRepository;
import com.kimyanu.blog.repository.PostRepository;
import com.kimyanu.blog.util.PageableFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService{

    private final List<String> SORTABLE_FIELDS = List.of("id", "content", "authorNickname", "createdAt");
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public PageResponse<CommentResponse> getCommentsByPostId(
            Long postId,
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortOrder) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다." + postId));

        Pageable pageDetails = PageableFactory.create(pageNumber, pageSize, sortBy, sortOrder, SORTABLE_FIELDS);
        Page<Comment> commentPage =  commentRepository.findByPost(post, pageDetails);
        List<Comment> comments = commentPage.getContent();

        return PageResponse.of(commentPage, commentMapper.toResponseList(comments));
    }

    @Transactional
    public CommentResponse createComment(Long memberId, Long postId, CommentRequest commentRequest) {

        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Comment comment = commentMapper.toEntity(commentRequest);
        comment.setPost(post);
        comment.setMember(member);
        Comment savedComment = commentRepository.save(comment);

        return commentMapper.toResponse(savedComment);
    }

    @Transactional
    public CommentResponse updateComment(Long memberId, Long commentId, CommentRequest commentRequest) {

        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        // == Access Control: Only the author of the post can update it
        if (!existingComment.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("댓글 작성자만 수정할 수 있습니다.");
        }

        // 간단하게 하는 방법
        existingComment.setContent(commentRequest.getContent());
        // 매퍼를 사용한 방법(하나밖에 안 바꾸는데 조금 과함? 기존 post 수정 방법과 동일)
//        commentMapper.updateEntityFromRequest(existingComment, commentRequest);

        return commentMapper.toResponse(existingComment);
    }

    @Transactional
    public void deleteComment(Long memberId, Long commentId) {

        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        // == Access Control: Only the author of the post can update it
        if (!existingComment.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("댓글 작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(existingComment);
    }
}
