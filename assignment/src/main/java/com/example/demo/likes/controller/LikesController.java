package com.example.demo.likes.controller;

import com.example.demo.likes.dto.LikesResponseDTO;
import com.example.demo.likes.service.LikesService;
import com.example.demo.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @Operation(
            summary = "게시글 좋아요 정보 조회",
            description = "주어진 게시글의 좋아요 수와 사용자가 좋아요를 눌렀는지 여부를 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")  // 인증 설정
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요 정보 조회 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @GetMapping("/posts/{postId}/likes")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<LikesResponseDTO> getLikeInfo(HttpSession session, @PathVariable long postId) {

        int likeCount = likesService.getLikesCount(postId);
        boolean isLiked = false;

        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        if(securityContext != null) {
            User user = (User) securityContext.getAuthentication().getPrincipal();
            if(user != null) {
                long userId = user.getId();
                isLiked = likesService.getIsLiked(postId, userId);
            }
        }

        LikesResponseDTO likesResponseDTO = new LikesResponseDTO();
        likesResponseDTO.setLikeCount(likeCount);
        likesResponseDTO.setLiked(isLiked);

        return ResponseEntity.ok(likesResponseDTO);
    }

    @Operation(
            summary = "게시글 좋아요 토글",
            description = "사용자가 게시글에 좋아요를 추가하거나 취소합니다. 로그인한 사용자만 접근할 수 있습니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요 상태가 성공적으로 변경됨"),
            @ApiResponse(responseCode = "400", description = "사용자가 로그인되지 않았거나 요청이 잘못됨"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @PostMapping("/posts/{postId}/likes")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<Void> toggleLikes(HttpSession session, @PathVariable long postId) {

        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        User user = (User) securityContext.getAuthentication().getPrincipal();
        if(user == null) {
            return ResponseEntity.badRequest().build();
        }

        likesService.toggleLikes(postId, user.getId());

        return ResponseEntity.ok().build();
    }
}
