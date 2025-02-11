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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final int postsPerPage = 8;

    @GetMapping(value = "/")
    public String showPostList(@RequestParam(required = false) Integer page, HttpSession session, Model model) {

        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        if(securityContext != null) {
            User user = (User) securityContext.getAuthentication().getPrincipal();
            model.addAttribute("user", user);
        }

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
        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");

        PostViewDTO postViewDTO = postService.getPostViewDTO(postId);

        if(securityContext != null) {
            User user = (User) securityContext.getAuthentication().getPrincipal();
            model.addAttribute("user", user);
        }

        model.addAttribute("post", postViewDTO);
        return "post";
    }

    // 게시글 작성
    @GetMapping(value = "/posts/add")
    @RolesAllowed({"ADMIN", "USER"})
    public String showAddPostPage(HttpSession session, Model model) {
        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        User user = (User) securityContext.getAuthentication().getPrincipal();

        PostCreateDTO postCreateDTO = new PostCreateDTO();

        model.addAttribute("user", user);
        model.addAttribute("postCreateDTO", postCreateDTO);

        return "add-post";
    }

    @PostMapping("/posts")
    @RolesAllowed({"ADMIN", "USER"})
    public String savePost(@ModelAttribute PostCreateDTO postCreateDTO, RedirectAttributes redirectAttributes, HttpSession session) {
        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        User user = (User) securityContext.getAuthentication().getPrincipal();

        if(user == null) {
            throw new NoSuchElementException();
        }

        postCreateDTO.setUserId(user.getId());
        Post savedPost = postService.savePost(postCreateDTO);
        redirectAttributes.addFlashAttribute("message", "게시글이 등록되었습니다.");

        return "redirect:/";
    }

    // 게시글 수정
    @GetMapping(value = "/posts/{postId}/edit")
    @PreAuthorize("hasRole('USER')")
    public String showEditPost(@PathVariable long postId, Model model, HttpSession session) throws AuthenticationException {
        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        if(securityContext == null) {
            throw new AccessDeniedException("권한이 없습니다");
        }

        User user = (User) securityContext.getAuthentication().getPrincipal();
        model.addAttribute("user", user);

        Post findPost = postService.findById(postId);
        long userId = findPost.getUser().getId();

        if(userId != user.getId()) {
            throw new AccessDeniedException("권한이 없습니다");
        }

        PostUpdateDTO postUpdateDTO = new PostUpdateDTO();
        postUpdateDTO.setId(findPost.getId());
        postUpdateDTO.setTitle(findPost.getTitle());
        postUpdateDTO.setContent(findPost.getContent());
        postUpdateDTO.setUserId(userId);

        model.addAttribute("postUpdateDTO", postUpdateDTO);

        return "edit-post";
    }

    @PostMapping("/posts/{postId}")
    @PreAuthorize("hasRole('USER') and #PostUpdateDTO.userId == authentication.id")
    public String updatePost(@PathVariable long postId, @ModelAttribute PostUpdateDTO postUpdateDTO, RedirectAttributes redirectAttributes) {
        postService.updatePost(postId, postUpdateDTO);

        redirectAttributes.addFlashAttribute("message", "게시글이 수정되었습니다.");
        return "redirect:/posts/{postId}";
    }

    // 게시글 삭제
    @GetMapping("/posts/{postId}/delete")
    @PreAuthorize("hasRole('USER')")
    public String deletePost(@PathVariable long postId, HttpSession session) throws AuthenticationException {

        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        User user = (User) securityContext.getAuthentication().getPrincipal();

        Post post = postService.findById(postId);
        if(post.getUser().getId() != user.getId()) {
            throw new AccessDeniedException("권한이 없습니다");
        }

        postService.deletePostById(postId) ;
        return "redirect:/";
    }
}
