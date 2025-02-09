package com.example.demo.post.controller;

import com.example.demo.post.dto.PostCreateDTO;
import com.example.demo.post.dto.PostListDTO;
import com.example.demo.post.dto.PostUpdateDTO;
import com.example.demo.post.dto.PostViewDTO;
import com.example.demo.post.model.Post;
import com.example.demo.post.service.PostService;
import com.example.demo.user.model.User;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.NoSuchElementException;

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
        PostListDTO postListDTO = postService.getPostListDTO(toShowPage, postsPerPage);
        model.addAttribute("postListDTO", postListDTO);

        model.addAttribute("currentPage", toShowPage);
        model.addAttribute("startPage", 0);
        model.addAttribute("endPage", 9);

        return "main";
    }

    // 게시글 상세
    @GetMapping("/posts/{postId}")
    public String showOnePost(@PathVariable long postId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        PostViewDTO postViewDTO = postService.getPostViewDTO(postId);
        model.addAttribute("post", postViewDTO);
        return "post";
    }

    // 게시글 삭제
    @GetMapping("/posts/{postId}/delete")
    public String deletePost(@PathVariable long postId, HttpSession session) {

        User user = (User) session.getAttribute("user");

        postService.deletePostById(postId) ;
        return "redirect:/";
    }

    @GetMapping(value = "/posts/add")
    // @PreAuthorize("hasRole('USER')")
    // @RolesAllowed({"ADMIN", "USER"})
    public String showAddPostPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        PostCreateDTO postCreateDTO = new PostCreateDTO();

        model.addAttribute("user", user);
        model.addAttribute("postCreateDTO", postCreateDTO);

        return "add-post";
    }

    // 게시글 작성
    @PostMapping("/posts")
    // @PreAuthorize("hasRole('USER')")
    public String savePost(@ModelAttribute PostCreateDTO postCreateDTO, RedirectAttributes redirectAttributes, HttpSession session) {

        System.out.println("postCreateDTO = " + postCreateDTO);

        User user = (User) session.getAttribute("user");
        if(user == null) {
            throw new NoSuchElementException();
        }

        postCreateDTO.setUserId(user.getId());
        Post savedPost = postService.savePost(postCreateDTO);
        redirectAttributes.addFlashAttribute("message", "게시글이 등록되었습니다.");

        return "redirect:/";
    }

    // 게시글 수정
    // 접근 권한 필요
    @GetMapping(value = "/posts/{postId}/edit")
    // @PreAuthorize("hasRole('USER') and #username == authentication.name")
    public String showEditPost(@PathVariable long postId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        Post findPost = postService.findById(postId);

        PostUpdateDTO postUpdateDTO = new PostUpdateDTO();
        postUpdateDTO.setId(findPost.getId());
        postUpdateDTO.setTitle(findPost.getTitle());
        postUpdateDTO.setContent(findPost.getContent());

        model.addAttribute("postUpdateDTO", postUpdateDTO);

        return "edit-post";
    }

    @PostMapping("/posts/{postId}")
    // @PreAuthorize("hasRole('USER') and #username == authentication.name")
    public String updatePost(@PathVariable long postId, @ModelAttribute PostUpdateDTO postUpdateDTO, RedirectAttributes redirectAttributes) {
        postService.updatePost(postId, postUpdateDTO);

        redirectAttributes.addFlashAttribute("message", "게시글이 수정되었습니다.");
        return "redirect:/posts/{postId}";
    }
}
