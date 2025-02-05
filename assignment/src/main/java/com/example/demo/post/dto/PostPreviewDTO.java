package com.example.demo.post.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class PostPreviewDTO {

    private String title;

    private String username;

    private int likes;

    private LocalDateTime createdAt;
}
