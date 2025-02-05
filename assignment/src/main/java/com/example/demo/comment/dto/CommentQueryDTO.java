package com.example.demo.comment.dto;

import com.example.demo.comment.model.Comment;
import com.example.demo.post.model.Post;
import com.example.demo.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
public class CommentQueryDTO {
    private long id;
    private String content;
    private long postId;
    private long parentId;
    private String userName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted = false;
}
