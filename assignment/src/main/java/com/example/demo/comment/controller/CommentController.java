package com.example.demo.comment.controller;

import com.example.demo.comment.dto.CommentDTO;
import com.example.demo.comment.dto.CommentViewDTO;
import com.example.demo.comment.model.Comment;
import com.example.demo.comment.service.CommentService;
import com.example.demo.post.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> saveComment(@RequestBody CommentDTO commentDTO) {

        Comment savedComment = commentService.createComment(commentDTO);

        return ResponseEntity.ok(savedComment);
    }

    @GetMapping
    public ResponseEntity<List<CommentViewDTO>> getComments(@PathVariable long postId) {

        List<CommentViewDTO> commentList = commentService.findByPostId(postId);
        return ResponseEntity.ok(commentList);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable long commentId, @RequestBody CommentDTO commentDTO) {

        commentService.updateComment(commentId, commentDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable long commentId) {

        return ResponseEntity.ok().build();
    }
}
