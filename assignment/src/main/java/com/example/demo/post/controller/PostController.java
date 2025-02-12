package com.example.demo.post.controller;

import com.example.demo.post.dto.PostCreateDTO;
import com.example.demo.post.dto.PostListDTO;
import com.example.demo.post.dto.PostUpdateDTO;
import com.example.demo.post.dto.PostViewDTO;
import com.example.demo.post.model.Post;
import com.example.demo.post.service.PostService;
import com.example.demo.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(
            summary = "게시글 목록 조회",
            description = "페이지네이션을 통해 게시글 목록을 조회합니다. 사용자가 로그인되어 있으면 사용자 정보를 함께 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
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
    @Operation(
            summary = "게시글 상세 조회",
            description = "게시글 ID를 이용하여 특정 게시글을 상세 조회합니다. 사용자가 로그인되어 있으면 사용자 정보를 함께 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
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
    @Operation(
            summary = "새 게시글 작성 페이지 조회",
            description = "새 게시글을 작성할 수 있는 페이지를 조회합니다. 로그인된 사용자만 접근 가능합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 작성 페이지 정상 반환"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
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

    @Operation(
            summary = "게시글 등록",
            description = "새 게시글을 등록합니다. 로그인된 사용자만 접근할 수 있습니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글이 성공적으로 등록됨"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @PostMapping("/posts")
    @RolesAllowed({"ADMIN", "USER"})
    public String savePost(@ModelAttribute PostCreateDTO postCreateDTO,
                           RedirectAttributes redirectAttributes,
                           HttpSession session) {
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
    @Operation(
            summary = "게시글 수정 페이지 조회",
            description = "사용자가 자신의 게시글을 수정할 수 있는 페이지를 조회합니다. 로그인된 사용자만 접근 가능하며, 수정 권한은 게시글 작성자에게만 주어집니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정 페이지 정상 반환"),
            @ApiResponse(responseCode = "403", description = "수정 권한 없음"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @GetMapping(value = "/posts/{postId}/edit")
    @PreAuthorize("hasRole('USER')")
    public String showEditPost(@PathVariable long postId, Model model, HttpSession session) {
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

    @Operation(
            summary = "게시글 수정",
            description = "게시글을 수정합니다. 수정 권한은 게시글 작성자에게만 부여됩니다. 수정 후 수정된 게시글 페이지로 리다이렉트됩니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            @ApiResponse(responseCode = "403", description = "수정 권한 없음"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @PostMapping("/posts/{postId}")
    @PreAuthorize("hasRole('USER') and #PostUpdateDTO.userId == authentication.id")
    public String updatePost(@PathVariable long postId,
                             @ModelAttribute PostUpdateDTO postUpdateDTO,
                             RedirectAttributes redirectAttributes) {
        postService.updatePost(postId, postUpdateDTO);

        redirectAttributes.addFlashAttribute("message", "게시글이 수정되었습니다.");
        return "redirect:/posts/{postId}";
    }

    // 게시글 삭제
    @Operation(
            summary = "게시글 삭제",
            description = "사용자가 자신의 게시글을 삭제할 수 있도록 합니다. 삭제 권한은 게시글 작성자에게만 부여됩니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "수정 권한 없음"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @GetMapping("/posts/{postId}/delete")
    @PreAuthorize("hasRole('USER')")
    public String deletePost(@PathVariable long postId, HttpSession session)  {

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
