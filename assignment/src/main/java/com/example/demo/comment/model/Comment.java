package com.example.demo.comment.model;

import com.example.demo.post.model.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
