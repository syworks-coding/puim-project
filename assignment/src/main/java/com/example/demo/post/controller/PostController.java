package com.example.demo.post.controller;

import com.example.demo.post.dto.PostDTO;
import com.example.demo.post.model.Post;
import com.example.demo.post.service.PostService;
import com.example.demo.user.model.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final int postsPerPage = 10;

    @GetMapping(value = "/")
    public String showPostList(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "main";
    }

    @GetMapping(value = "/posts")
    public ResponseEntity<List<Post>> getPostListByPage(@RequestParam int page, HttpSession session, Model model) {

        List<Post> postList = postService.getPostList(page, postsPerPage);
        return ResponseEntity.ok(postList);
    }

    @GetMapping("/posts/{postId}")
    public String showOnePost(@PathVariable long postId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        Post post = postService.findById(postId);
        model.addAttribute("post", post);

        return "post";
    }

    @GetMapping(value = "/posts/add")
    public String showAddPostPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        return "add-post";
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> savePost(@RequestBody PostDTO postDTO) {
        Post savedPost = postService.savePost(postDTO);

        return ResponseEntity.ok(savedPost);
    }

    @PatchMapping("/posts/{postId}")
    public void updatePost(@PathVariable long postId, @RequestBody PostDTO postDTO) {
        postService.updatePost(postId, postDTO);
    }
}
