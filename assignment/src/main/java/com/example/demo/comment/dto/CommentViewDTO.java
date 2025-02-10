package com.example.demo.comment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class CommentViewDTO {

    private Long id;
    private Long postId;
    private Long userId;
    private String username;
    private String content;
    private List<CommentViewDTO> replies;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
}
