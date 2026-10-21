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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService{

    private final List<String> SORTABLE_FIELDS = List.of("id", "title", "content", "createdAt");
    private final List<String> SORTABLE_MY_FIELDS = List.of("id", "title", "content", "createdAt", "isVisible");
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;

    // select ... from post - 1건
    // select count(...) from post ... - 1건
    // select count(...) from comment where post_id = ? - 1건
    // select count(...) from likes where post_id = ? - 1건
    // 게시글이 10개, 2건 + (1건 * 2) * 10 = 22건 -> 2N + 2
    // 에서 프로젝션 써서 4건으로 줄임
    public PageResponse<PostResponse> getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageDetails = PageableFactory.create(pageNumber, pageSize, sortBy, sortOrder, SORTABLE_FIELDS);
        Page<Post> postPage = postRepository.findByIsVisibleTrue(pageDetails);
        List<Post> posts = postPage.getContent();

        return PageResponse.of(postPage, toResponseListWithCounts(posts));
    }

    public PageResponse<PostResponse> getAllPostsForAdmin(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageDetails = PageableFactory.create(pageNumber, pageSize, sortBy, sortOrder, SORTABLE_FIELDS);
        Page<Post> postPage = postRepository.findAllBy(pageDetails);
        List<Post> posts = postPage.getContent();

        return PageResponse.of(postPage, toResponseListWithCounts(posts));
    }

    // 감춘 글이어도 작성자 본인이나 관리자는 볼 수 있다.

    public PageResponse<PostResponse> getMyPosts(Long memberId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Pageable pageDetails = PageableFactory.create(pageNumber, pageSize, sortBy, sortOrder, SORTABLE_MY_FIELDS);
        Page<Post> postPage = postRepository.findByMemberId(memberId, pageDetails);
        List<Post> posts = postPage.getContent();

        return PageResponse.of(postPage, toResponseListWithCounts(posts));
    }
    public PostResponse getPostById(Long memberId, Long postId, MemberRole role) {

        Post post = postRepository.findWithMemberAndCategoryById(postId)
                .orElseThrow(PostNotFoundException::new);

        boolean isAuthor = post.getMember().getId().equals(memberId);
        boolean isAdmin = role == MemberRole.ADMIN;

        if (!post.getIsVisible() && !isAuthor && !isAdmin) {
            throw new PostNotFoundException();
        }

        return toResponseWithCounts(post);
    }

    @Transactional
    public PostResponse createPost(PostRequest postRequest, Long memberId) {

        Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Category category = categoryRepository.findById(postRequest.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);

        Post postEntity = postMapper.toEntity(postRequest);
        postEntity.setMember(member);
        postEntity.setCategory(category);

        Post savedPost = postRepository.save(postEntity);
        PostResponse response = postMapper.toResponse(savedPost);
        response.setCommentCount(0L);
        response.setLikesCount(0L);
        return toResponseWithCounts(savedPost);
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

        return toResponseWithCounts(existingPost);
    }

    @Transactional
    public PostResponse updatePostVisibility(Long postId, PostVisibilityRequest postVisibilityRequest) {

        Post existingPost = postRepository.findWithMemberAndCategoryById(postId)
                .orElseThrow(PostNotFoundException::new);


        existingPost.setIsVisible(postVisibilityRequest.getIsVisible());
        return toResponseWithCounts(existingPost);
    }

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

    private PostResponse toResponseWithCounts(Post post) {

        PostResponse response = postMapper.toResponse(post);
        response.setCommentCount(commentRepository.countByPostId(post.getId()));
        response.setLikesCount(likesRepository.countByPostId(post.getId()));

        return response;
    }

    // postId | total
    // 1      | 3
    // 2      | 5
    private Map<Long, Long> toCountMap(List<PostCount> counts) {
        return counts.stream().collect(Collectors.toMap(PostCount::getPostId, PostCount::getTotal));
    }

    private List<PostResponse> toResponseListWithCounts(List<Post> posts) {
        List<PostResponse> responses = postMapper.toResponseList(posts);
        if(responses.isEmpty()) {
            return responses;
        }

        // SELECT POST_ID, COUNT(ID) FROM COMMENT WHERE POST_ID IN (1, 2, 3) GROUP BY POST_ID;
        List<Long> postIds = responses.stream().map(PostResponse::getId).toList();

        // <키, 값> = <게시글 번호, 댓글 수>
        // <1, 3>, <2, 5>, <3, 0>
        Map<Long, Long> commentCounts = toCountMap(commentRepository.countByPostIds(postIds));

        // <키, 값> = <게시글 번호, 좋아요 수>
        // <1, 3>, <2, 5>, <3, 0>
        Map<Long, Long> likesCounts = toCountMap(likesRepository.countByPostIds(postIds));

        // 결과에 없는 게시글은 댓글이나 좋아요가 없는 것이다
        // Map.getOrDefault(key, defaultValue) : key에 해당하는 값이 있으면 그 값을 반환하고, 없으면 defaultValue를 반환한다.
        for (PostResponse response : responses) {
            response.setCommentCount(commentCounts.getOrDefault(response.getId(), 0L));
            response.setLikesCount(likesCounts.getOrDefault(response.getId(), 0L));
        }
        return responses;
    }
}
