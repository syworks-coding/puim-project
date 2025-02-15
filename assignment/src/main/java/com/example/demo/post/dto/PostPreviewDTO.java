package com.example.demo.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class PostPreviewDTO { // 게시물 목록 건당

    @Schema(description = "게시물 ID", example = "1")
    private long id;

    @Schema(description = "게시물 제목", example = "Spring Boot 게시물 작성하기")
    private String title;

    @Schema(description = "작성자 이름", example = "홍길동")
    private String username;

    @Schema(description = "좋아요 수", example = "100")
    private int likes;

    @Schema(description = "댓글 수", example = "10")
    private int comments;

    @Schema(description = "게시물 작성 시간", example = "2025-02-12T14:30:00")
    private LocalDateTime createdAt;
}