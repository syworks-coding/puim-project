package com.example.demo.comment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentDTO { // 댓글 작성용

    @Size(min = 1, max = 200, message = "내용을 입력해주세요.")
    private String content;

    @NotNull
    private long userId;

    @NotNull
    private Long postId;

    private Long parentId;
}
