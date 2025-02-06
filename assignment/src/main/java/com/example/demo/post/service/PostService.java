package com.example.demo.post.service;

import com.example.demo.likes.repository.LikesRepository;
import com.example.demo.post.dto.PostDTO;
import com.example.demo.post.dto.PostPreviewDTO;
import com.example.demo.post.model.Post;
import com.example.demo.post.repository.PostRepository;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
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
    private final LikesRepository likesRepository;

    public List<Post> getPostList(int page, int postsPerPage) {

        Page<Post> postListPage = postRepository.findAll(
                PageRequest.of(page, postsPerPage, Sort.by(Sort.Order.asc("id")))
        );

        return postListPage.get().toList();
    }

    public Post findById(long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public Post savePost(PostDTO postDTO) {
        Post post = new Post();
        System.out.println("postDTO.getUsername() = " + postDTO.getUsername());
        User user = userRepository.findByUserId(postDTO.getUsername()).orElseThrow();

        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setUser(user);

        return postRepository.save(post);
    }

    public void updatePost(long postId, PostDTO postDTO) {
        Post post = postRepository.findById(postId).orElseThrow();

        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());

        postRepository.save(post);
    }

    public void deletePost(long postId) {
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
}
