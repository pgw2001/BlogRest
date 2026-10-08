package com.kimyanu.blog.service;

import com.kimyanu.blog.dto.post.PostDto;
import com.kimyanu.blog.dto.post.PostRequest;
import com.kimyanu.blog.dto.post.PostResponse;
import com.kimyanu.blog.exceptions.CategoryNotFoundException;
import com.kimyanu.blog.mapper.PostMapper;
import com.kimyanu.blog.model.Category;
import com.kimyanu.blog.model.Member;
import com.kimyanu.blog.model.Post;
import com.kimyanu.blog.repository.CategoryRepository;
import com.kimyanu.blog.repository.MemberRepository;
import com.kimyanu.blog.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static com.kimyanu.blog.util.PageableFactory.getPageable;

@Service
public class PostServiceImpl implements PostService{

    private final List<String> SORTABLE_FIELDS = List.of("id", "title", "content", "createdAt");
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper, MemberRepository memberRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
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

    @Override
    public PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageDetails = getPageable(pageNumber, pageSize, sortBy, sortOrder, SORTABLE_FIELDS);
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

    @Override
    public PostDto getPostById(Long postId) {

        Post post = postRepository.findWithMemberAndCategoryById(postId)
                .filter(Post::getIsVisible)
                .orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다 : " + postId));

        return postMapper.toDto(post);
    }

    @Override
    public void deletePost(Long postId) {

        Post existingPost = postRepository.findWithMemberAndCategoryById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다 : " + postId));

        postRepository.delete(existingPost);
    }

    @Override
    @Transactional
    public PostDto updatePost(Long postId, PostRequest postRequest) {

        Post existingPost = postRepository.findWithMemberAndCategoryById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다 : " + postId));

        if(postRequest.getCategoryId() != null && !postRequest.getCategoryId().equals(existingPost.getCategory().getId())) {
            Category category = categoryRepository.findById(postRequest.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(postRequest.getCategoryId()));
            existingPost.setCategory(category);
        }

        postMapper.updateEntityFromRequest(existingPost, postRequest);

        return postMapper.toDto(existingPost);
    }
}
