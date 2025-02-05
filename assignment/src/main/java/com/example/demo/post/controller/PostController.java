package com.example.demo.post.controller;

import com.example.demo.post.dto.PostDTO;
import com.example.demo.post.dto.PostPreviewDTO;
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
    private final int postsPerPage = 8;

    @GetMapping(value = "/")
    public String showPostList(@RequestParam(required = false) Integer page, HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        int toShowPage = page == null ? 0 : page - 1;
        List<Post> postList = postService.getPostList(toShowPage, postsPerPage);
        model.addAttribute("postPreviewDTOList", postList);
        model.addAttribute("postCount", postList.size());

        model.addAttribute("currentPage", toShowPage);
        model.addAttribute("startPage", 0);
        model.addAttribute("endPage", 9);

        return "main";
    }

    // 접근 권한 필요
    @GetMapping(value = "/posts/{postId}/edit")
    public String showEditPost(@PathVariable long postId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        Post post = postService.findById(postId);
        model.addAttribute("post", post);

        return "edit-post";
    }

    @GetMapping(value = "/posts")
    public ResponseEntity<List<PostPreviewDTO>> getPostListByPage(@RequestParam(required = false) int page, HttpSession session, Model model) {

        List<PostPreviewDTO> postList = postService.getPostPreviewList(page - 1, postsPerPage);
        return ResponseEntity.ok(postList);
    }

    // 기본 목록 조회
    @GetMapping(value = "/posts-basic")
    public ResponseEntity<List<Post>> getPostListByPageBasic(@RequestParam(required = false) int page, HttpSession session, Model model) {

        List<Post> postList = postService.getPostList(page - 1, postsPerPage);
        return ResponseEntity.ok(postList);
    }



    @GetMapping("/posts/{postId}")
    public String showOnePost(@PathVariable long postId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        Post post = postService.findById(postId);
        model.addAttribute("post", post);
        //바꿔바꿔바꿔바꿔바꿔바꿔바꿔바꿔바꿔바꿔바꿔바꿔바꿔바꿔바꿔바꿔바꿔바꿔바꿔바꿔바꿔바꿔바꿔바꿔바꿔바꿔
        model.addAttribute("likes", 12);
        model.addAttribute("commentsCount", 11);

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
    public ResponseEntity<Void> updatePost(@PathVariable long postId, @RequestBody PostDTO postDTO) {
        postService.updatePost(postId, postDTO);

        return ResponseEntity.ok().build();
    }
}
