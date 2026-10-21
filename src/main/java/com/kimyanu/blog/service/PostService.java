package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.common.PageResponse;
import com.kimyanu.blog.dto.post.PostRequest;
import com.kimyanu.blog.dto.post.PostResponse;
import com.kimyanu.blog.dto.post.PostVisibilityRequest;
import com.kimyanu.blog.enums.MemberRole;
import com.kimyanu.blog.exceptions.custom.CategoryNotFoundException;
import com.kimyanu.blog.exceptions.custom.ForbiddenException;
import com.kimyanu.blog.exceptions.custom.MemberNotFoundException;
import com.kimyanu.blog.exceptions.custom.PostNotFoundException;
import com.kimyanu.blog.mapper.PostMapper;
import com.kimyanu.blog.model.Category;
import com.kimyanu.blog.model.Member;
import com.kimyanu.blog.model.Post;
import com.kimyanu.blog.repository.*;
import com.kimyanu.blog.util.PageableFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService{

    private final List<String> SORTABLE_FIELDS = List.of("id", "title", "content", "createdAt");
    private final List<String> SORTABLE_MY_FIELDS = List.of("id", "title", "content", "createdAt", "isVisible");
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;

    public PageResponse<PostResponse> getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageDetails = PageableFactory.create(pageNumber, pageSize, sortBy, sortOrder, SORTABLE_FIELDS);
        Page<Post> postPage = postRepository.findByIsVisibleTrue(pageDetails);
        List<Post> posts = postPage.getContent();

        return PageResponse.of(postPage, postMapper.toResponseList(posts));
    }

    public PageResponse<PostResponse> getAllPostsForAdmin(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageDetails = PageableFactory.create(pageNumber, pageSize, sortBy, sortOrder, SORTABLE_FIELDS);
        Page<Post> postPage = postRepository.findAllBy(pageDetails);
        List<Post> posts = postPage.getContent();

        return PageResponse.of(postPage, postMapper.toResponseList(posts));
    }

    // 감춘 글이어도 작성자 본인이나 관리자는 볼 수 있다.
    public PostResponse getPostById(Long memberId, Long postId, MemberRole role) {

        Post post = postRepository.findWithMemberAndCategoryById(postId)
                .orElseThrow(PostNotFoundException::new);

        boolean isAuthor = post.getMember().getId().equals(memberId);
        boolean isAdmin = role == MemberRole.ADMIN;

        if (!post.getIsVisible() && !isAuthor && !isAdmin) {
            throw new PostNotFoundException();
        }

        return postMapper.toResponse(post);
    }

    public PageResponse<PostResponse> getMyPosts(Long memberId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Pageable pageDetails = PageableFactory.create(pageNumber, pageSize, sortBy, sortOrder, SORTABLE_MY_FIELDS);
        Page<Post> postPage = postRepository.findByMemberId(memberId, pageDetails);
        List<Post> posts = postPage.getContent();

        return PageResponse.of(postPage, postMapper.toResponseList(posts));
    }

    @Transactional
    public PostResponse createPost(PostRequest postRequest, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Category category = categoryRepository.findById(postRequest.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);

        Post postEntity = postMapper.toEntity(postRequest);
        postEntity.setMember(member);
        postEntity.setCategory(category);

        Post savedPost = postRepository.save(postEntity);
        return postMapper.toResponse(savedPost);
    }

    @Transactional
    public PostResponse updatePost(Long memberId, Long postId, PostRequest postRequest) {

        Post existingPost = findOwnPost(memberId, postId);

        if(postRequest.getCategoryId() != null && !postRequest.getCategoryId().equals(existingPost.getCategory().getId())) {
            Category category = categoryRepository.findById(postRequest.getCategoryId())
                    .orElseThrow(CategoryNotFoundException::new);
            existingPost.setCategory(category);
        }

        postMapper.updateEntityFromRequest(existingPost, postRequest);

        return postMapper.toResponse(existingPost);
    }

    @Transactional
    public PostResponse updatePostVisibility(Long postId, PostVisibilityRequest postVisibilityRequest) {

        Post existingPost = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);


        existingPost.setIsVisible(postVisibilityRequest.getIsVisible());
        return postMapper.toResponse(existingPost);
    }

    // 1. 게시글을 삭제할 때 물리 삭제 (이 프로젝트에서는 물리 삭제)
    // 1.1. 댓글을 먼저 지우고, 좋아요를 지우고, 마지막으로 게시글을 지운다.
    // 1.1.1. A라는 사람이 B라는 사람의 게시글에 댓글을 달았어요.
    // 1.1.2. 마이페이지에서는 내가 남긴 댓글 목록이 보이는 페이지가 있다고 가정.
    // 1.1.3. 거기서 B라는 사람의 게시글에남긴 댓글이 보이고 있음.
    // 1.1.4. 그런데 어느날 B라는 사람이 게시글이 삭제되었기 때문에 더 이상 존재하지 않는 게기글에 대한 댓글이 되어버립니다.
    // 1.1.5. A라는 사람의 댓글은 게시글이 삭제되었기 때문에 더 이상 존재하지 않는 게시글에 대한 댓글이 되어버립니다.
    // 1.1.6. 따라서 게시글을 삭제할 때 댓글도 함께 삭제해야 한다.
    // 1.1.7. 이렇게 될 경우 A라는 사람 입장에서는 갑자기 내가 보던 댓글 목록의 갯수가 1개 줄어든 것처럼 보이게 됨.

    // 2. 게시글을 삭제할 때 논리 삭제
    // 2.1. deletedAt 컬럼을 추가하고, 게시글을 삭제할 때 deletedAt 컬럼에 현재 시간을 넣어주고,
    //                게시글을 조회할 때, deletedAt 컬럼이 null인 게시글만 조회하도록 한다.
    // 2.2. 이렇게 하면 게시글을 삭제하더라도 댓글은 그대로 남아있게 된다.
    // 2.3. 하지만 게시글이 삭제되었기 떄문에 댓글은 더 이상 존해자지 않는 게시글에 대한 댓글이 되어버린다.
    // 2.4. 따라서 이를 위한 정책 및 비즈니스 로직에 대한 고민이 필요하다. (원본 글 삭제됨)


    @Transactional
    public void deletePost(Long memberId, Long postId) {

        Post existingPost = findOwnPost(memberId, postId);

        commentRepository.deleteAllByPostId(postId);
        likesRepository.deleteAllByPostId(postId);
        postRepository.delete(existingPost);
    }

    private Post findOwnPost(Long memberId, Long postId) {

        Post post = postRepository.findWithMemberAndCategoryById(postId)
                .orElseThrow(PostNotFoundException::new);

        if (!post.getMember().getId().equals(memberId)) {
            if(!post.getIsVisible()) {
                throw new PostNotFoundException();
            }
        }

        if (!post.getMember().getId().equals(memberId)) {
            throw new ForbiddenException("게시글 작성자만 수정할 수 있습니다.");
        }

        return post;
    }
}
