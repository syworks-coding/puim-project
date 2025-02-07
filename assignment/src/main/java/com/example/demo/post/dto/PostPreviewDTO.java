package com.example.demo.post.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class PostPreviewDTO { // 게시물 목록 건당

    private long id;

    private String title;

    private String username;

    private int likes;

    private int comments;

    private LocalDateTime createdAt;
}
