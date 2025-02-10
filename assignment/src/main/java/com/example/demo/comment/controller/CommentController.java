package com.example.demo.comment.controller;

import com.example.demo.comment.dto.CommentDTO;
import com.example.demo.comment.dto.CommentResponseDTO;
import com.example.demo.comment.model.Comment;
import com.example.demo.comment.service.CommentService;
import com.example.demo.user.model.User;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 조회
    @GetMapping
    public ResponseEntity<CommentResponseDTO> getComments(@PathVariable long postId, HttpSession session) {

        CommentResponseDTO commentResponseDTO = commentService.findByPostId(postId);
        return ResponseEntity.ok(commentResponseDTO);
    }

    // 댓글 작성
    @PostMapping
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<Comment> saveComment(@PathVariable long postId, @RequestBody CommentDTO commentDTO, HttpSession session) {

        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        User user = (User) securityContext.getAuthentication().getPrincipal();
        commentDTO.setUserId(user.getId());
        Comment savedComment = commentService.createComment(commentDTO);

        return ResponseEntity.ok(savedComment);
    }

    @PatchMapping("/{commentId}")
    @PreAuthorize("hasRole('USER') and #CommentDTO.userId == authentication.id")
    public ResponseEntity<Void> updateComment(@PathVariable long postId, @PathVariable long commentId, @RequestBody CommentDTO commentDTO) {

        commentService.updateComment(commentId, commentDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteComment(@PathVariable long postId, @PathVariable long commentId, HttpSession session) throws AuthenticationException {
        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        User user = (User) securityContext.getAuthentication().getPrincipal();

        Comment comment = commentService.findByCommentId(commentId);
        if(user.getId() != comment.getUser().getId()) {
            throw new AuthenticationException();
        }

        commentService.deleteCommentById(commentId);
        return ResponseEntity.ok().build();
    }
}
