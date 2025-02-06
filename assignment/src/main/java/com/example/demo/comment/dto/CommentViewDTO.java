package com.example.demo.comment.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CommentViewDTO {

    private Long postId;
    private String username;
    private String content;
    private List<CommentViewDTO> replies;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
}
