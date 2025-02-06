package com.example.demo.likes.controller;

import com.example.demo.likes.service.LikesService;
import com.example.demo.user.model.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @GetMapping("/posts/{postId}/likes")
    public ResponseEntity<Map<String, Object>> getLikeInfo(HttpSession session, @PathVariable long postId) {

        int likeCount = likesService.getLikesCount(postId);
        boolean isLiked = false;

        User user = (User) session.getAttribute("user");
        if(user != null) {
            long userId = user.getId();
            isLiked = likesService.getIsLiked(postId, userId);
        }

        Map<String, Object> response= new HashMap<>();
        response.put("likeCount", likeCount);
        response.put("isLiked", isLiked);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/posts/{postId}/likes")
    public ResponseEntity<Void> toggleLikes(HttpSession session, @PathVariable long postId) {

        User user = (User) session.getAttribute("user");
        if(user == null) {
            return ResponseEntity.badRequest().build();
        }

        likesService.toggleLikes(postId, user.getId());

        return ResponseEntity.ok().build();
    }
}
