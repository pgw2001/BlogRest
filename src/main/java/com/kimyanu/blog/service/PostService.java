package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.post.PostDto;
import com.kimyanu.blog.dto.post.PostRequest;
import com.kimyanu.blog.dto.post.PostResponse;
import com.kimyanu.blog.dto.post.PostVisibilityRequest;
import com.kimyanu.blog.exceptions.CategoryNotFoundException;
import com.kimyanu.blog.mapper.PostMapper;
import com.kimyanu.blog.model.Category;
import com.kimyanu.blog.model.Member;
import com.kimyanu.blog.model.Post;
import com.kimyanu.blog.repository.CategoryRepository;
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
public class PostService{

    private final List<String> SORTABLE_FIELDS = List.of("id", "title", "content", "createdAt");
    private final List<String> SORTABLE_MY_FIELDS = List.of("id", "title", "content", "createdAt", "isVisible");
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    public PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageDetails = PageableFactory.create(pageNumber, pageSize, sortBy, sortOrder, SORTABLE_FIELDS);
        Page<Post> postPage = postRepository.findByIsVisibleTrue(pageDetails);
        List<Post> posts = postPage.getContent();

        return new PostResponse(
                postMapper.toDtoList(posts),
                postPage.getNumber() + 1,
                postPage.getSize(),
                postPage.getTotalElements(),
                postPage.getTotalPages(),
                postPage.isLast()
        );
    }

    public PostResponse getAllPostsForAdmin(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageDetails = PageableFactory.create(pageNumber, pageSize, sortBy, sortOrder, SORTABLE_FIELDS);
        Page<Post> postPage = postRepository.findAllBy(pageDetails);
        List<Post> posts = postPage.getContent();

        return new PostResponse(
                postMapper.toDtoList(posts),
                postPage.getNumber() + 1,
                postPage.getSize(),
                postPage.getTotalElements(),
                postPage.getTotalPages(),
                postPage.isLast()
        );
    }

    public PostDto getPostById(Long postId) {

        Post post = postRepository.findWithMemberAndCategoryById(postId)
                .filter(Post::getIsVisible)
                .orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다 : " + postId));

        return postMapper.toDto(post);
    }

    public PostResponse getMyPosts(Long memberId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다." + memberId));


        Pageable pageDetails = PageableFactory.create(pageNumber, pageSize, sortBy, sortOrder, SORTABLE_FIELDS);
        Page<Post> postPage = postRepository.findByMemberId(memberId, pageDetails);
        List<Post> posts = postPage.getContent();

        return new PostResponse(
                postMapper.toDtoList(posts),
                postPage.getNumber() + 1,
                postPage.getSize(),
                postPage.getTotalElements(),
                postPage.getTotalPages(),
                postPage.isLast()
        );
    }

    @Transactional(readOnly = true)
    public PostDto createPost(PostRequest postRequest, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 회원을 찾을 수 없습니다. : " + memberId));

        Category category = categoryRepository.findById(postRequest.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(postRequest.getCategoryId()));

        Post postEntity = postMapper.toEntity(postRequest);
        postEntity.setMember(member);
        postEntity.setCategory(category);

        Post savedPost = postRepository.save(postEntity);
        return postMapper.toDto(savedPost);
    }

    @Transactional
    public PostDto updatePost(Long memberId, Long postId, PostRequest postRequest) {

        Post existingPost = postRepository.findWithMemberAndCategoryById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다 : " + postId));

        // == Access Control: Only the author of the post can update it
        if (!existingPost.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("게시글 작성자만 수정할 수 있습니다.");
        }

        if(postRequest.getCategoryId() != null && !postRequest.getCategoryId().equals(existingPost.getCategory().getId())) {
            Category category = categoryRepository.findById(postRequest.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(postRequest.getCategoryId()));
            existingPost.setCategory(category);
        }

        postMapper.updateEntityFromRequest(existingPost, postRequest);

        return postMapper.toDto(existingPost);
    }

    public PostDto updatePostVisibility(Long postId, PostVisibilityRequest postVisibilityRequest) {

        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다. " + postId));


        existingPost.setIsVisible(postVisibilityRequest.getIsVisible());
        return postMapper.toDto(existingPost);
    }

    public void deletePost(Long memberId, Long postId) {

        Post existingPost = postRepository.findWithMemberAndCategoryById(postId)
                .filter(post -> post.getMember().getId().equals(memberId))
                .orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다 : " + postId));

        postRepository.delete(existingPost);
    }
}
