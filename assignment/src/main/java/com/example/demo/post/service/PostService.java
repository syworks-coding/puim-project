package com.example.demo.post.service;

import com.example.demo.comment.dto.CommentViewDTO;
import com.example.demo.comment.service.CommentService;
import com.example.demo.likes.service.LikesService;
import com.example.demo.post.dto.PostCreateDTO;
import com.example.demo.post.dto.PostPreviewDTO;
import com.example.demo.post.dto.PostUpdateDTO;
import com.example.demo.post.dto.PostViewDTO;
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
    private final UserRepository userRepository;
    private final LikesService likesService;
    private final CommentService commentService;

    public List<Post> getPostList(int page, int postsPerPage) {

        Page<Post> postListPage = postRepository.findAll(
                PageRequest.of(page, postsPerPage, Sort.by(Sort.Order.desc("id")))
        );

        return postListPage.get().toList();
    }

    public Post findById(long postId) {
        return postRepository.findById(postId).orElse(null);
    }


    public Post savePost(PostCreateDTO postCreateDTO) {
        Post post = new Post();
        User user = userRepository.findById(postCreateDTO.getUserId()).orElseThrow();

        post.setTitle(postCreateDTO.getTitle());
        post.setContent(postCreateDTO.getContent());
        post.setUser(user);

        return postRepository.save(post);
    }

    public void updatePost(long postId, PostUpdateDTO postUpdateDTO) {
        Post post = postRepository.findById(postId).orElseThrow();

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

    //
    public List<PostPreviewDTO> getPostPreviewList(int page, int postsPerPage) {
        Page<Post> postListPage = postRepository.findAll(
                PageRequest.of(page, postsPerPage, Sort.by(Sort.Order.desc("createdAt")))
        );

        List<PostPreviewDTO> postPreviewDTOList = new ArrayList<>();
       // likesRepository.countByPostId();

        return postPreviewDTOList;
    }

    public PostViewDTO getPostViewDTO(long postId) {

        Post post = postRepository.findById(postId).orElseThrow();
        List<CommentViewDTO> commentViewDTOS = commentService.findCommentViewDTOS(postId);

        PostViewDTO postViewDTO = new PostViewDTO();

        postViewDTO.setId(post.getId());
        postViewDTO.setTitle(post.getTitle());
        postViewDTO.setContent(post.getContent());
        postViewDTO.setCreatedAt(post.getCreatedAt());
        postViewDTO.setUserId(post.getUser().getUserId());
        postViewDTO.setComments(commentViewDTOS);

        return postViewDTO;
    }
}
