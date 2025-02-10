package com.example.demo.likes.controller;

import com.example.demo.likes.service.LikesService;
import com.example.demo.user.model.User;
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

    @GetMapping("/posts/{postId}/likes")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<Map<String, Object>> getLikeInfo(HttpSession session, @PathVariable long postId) {

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

        Map<String, Object> response= new HashMap<>();
        response.put("likeCount", likeCount);
        response.put("isLiked", isLiked);

        return ResponseEntity.ok(response);
    }

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
