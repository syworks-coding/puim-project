package com.example.demo.post.service;

import com.example.demo.comment.dto.CommentViewDTO;
import com.example.demo.comment.service.CommentService;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.likes.service.LikesService;
import com.example.demo.mapper.PostMapper;
import com.example.demo.post.dto.*;
import com.example.demo.post.model.Post;
import com.example.demo.post.repository.PostRepository;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    private final UserRepository userRepository;
    private final LikesService likesService;
    private final CommentService commentService;

    public PostListDTO getPostListDTO(int page, int postsPerPage) {

        PostListDTO postListDTO = new PostListDTO();
        List<PostPreviewDTO> postPreviewDTOList = postMapper.findPostInfoById(postsPerPage, page * postsPerPage);
        postListDTO.setPosts(postPreviewDTOList);

        long totalCount = postRepository.count();
        postListDTO.setTotalCount(totalCount);
        postListDTO.setTotalPage(totalCount / postsPerPage + 1);


        return postListDTO;
    }

    public Post findById(long postId) {
        return postRepository.findById(postId).orElse(null);
    }


    @Transactional
    public Post savePost(PostCreateDTO postCreateDTO) {
        Post post = new Post();
        User user = userRepository.findById(postCreateDTO.getUserId()).orElseThrow();

        post.setTitle(postCreateDTO.getTitle());
        post.setContent(postCreateDTO.getContent());
        post.setUser(user);

        return postRepository.save(post);
    }

    @Transactional
    public void updatePost(long postId, PostUpdateDTO postUpdateDTO) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시글 입니다."));

        post.setTitle(postUpdateDTO.getTitle());
        post.setContent(postUpdateDTO.getContent());

        postRepository.save(post);
    }

    @Transactional
    public void deletePostsByUserId(long userId) {
        List<Post> userPostList = postRepository.findByUserId(userId);

        for (Post post : userPostList) {
            deletePostById(post.getId());
        }
    }

    @Transactional
    public void deletePostById(long postId) {
        likesService.deleteByPostId(postId);
        commentService.deleteByPostId(postId);
        postRepository.deleteById(postId);
    }

    public PostViewDTO getPostViewDTO(long postId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시글 입니다."));

        PostViewDTO postViewDTO = new PostViewDTO();

        postViewDTO.setId(post.getId());
        postViewDTO.setTitle(post.getTitle());
        postViewDTO.setContent(post.getContent());
        postViewDTO.setCreatedAt(post.getCreatedAt());
        postViewDTO.setUpdatedAt(post.getUpdatedAt());
        postViewDTO.setUserId(post.getUser().getId());
        postViewDTO.setUsername(post.getUser().getUsername());

        return postViewDTO;
    }

    public Long getUserIdByPostId(long postId) {
        return postRepository.findById(postId).orElseThrow().getUser().getId();
    }
}
