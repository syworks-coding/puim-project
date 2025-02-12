package com.example.demo.comment.controller;

import com.example.demo.comment.dto.CommentDTO;
import com.example.demo.comment.dto.CommentResponseDTO;
import com.example.demo.comment.model.Comment;
import com.example.demo.comment.service.CommentService;
import com.example.demo.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(
            summary = "게시글 댓글 조회",
            description = "주어진 게시글에 대한 댓글 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @GetMapping
    public ResponseEntity<CommentResponseDTO> getComments(@PathVariable long postId, HttpSession session) {

        CommentResponseDTO commentResponseDTO = commentService.findByPostId(postId);
        return ResponseEntity.ok(commentResponseDTO);
    }

    // 댓글 작성
    @Operation(
            summary = "게시글 댓글 작성",
            description = "게시글에 새로운 댓글을 작성합니다. 사용자는 로그인 후 댓글을 작성할 수 있습니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "댓글 작성 실패 (잘못된 입력 등)"),
            @ApiResponse(responseCode = "403", description = "사용자 권한 없음 (로그인하지 않은 경우)")
    })
    @PostMapping
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<Void> saveComment(@PathVariable long postId, @RequestBody CommentDTO commentDTO, HttpSession session) {

        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        User user = (User) securityContext.getAuthentication().getPrincipal();
        commentDTO.setUserId(user.getId());
        commentService.createComment(commentDTO);

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "댓글 수정",
            description = "댓글을 수정합니다. 사용자만 자신의 댓글을 수정할 수 있습니다.",
            security = @SecurityRequirement(name = "bearerAuth")  // JWT 인증을 사용하는 경우
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (잘못된 입력 데이터 등)"),
            @ApiResponse(responseCode = "403", description = "권한 없음 (자신의 댓글이 아닌 경우)"),
    })
    @PatchMapping("/{commentId}")
    @PreAuthorize("hasRole('USER') and #CommentDTO.userId == authentication.id")
    public ResponseEntity<Void> updateComment(@PathVariable long postId, @PathVariable long commentId, @RequestBody CommentDTO commentDTO) {

        commentService.updateComment(commentId, commentDTO);
        return ResponseEntity.ok().build();
    }


    @Operation(
            summary = "댓글 삭제",
            description = "사용자는 자신의 댓글을 삭제할 수 있습니다.",
            security = @SecurityRequirement(name = "bearerAuth")  // JWT 인증을 사용하는 경우
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (잘못된 댓글 ID 등)"),
            @ApiResponse(responseCode = "403", description = "권한 없음 (자신의 댓글이 아닌 경우)"),
    })
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
