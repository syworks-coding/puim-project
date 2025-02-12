package com.example.demo.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentDTO { // 댓글 작성용

    @Schema(description = "댓글 내용", example = "이것은 댓글 내용입니다.", required = true)
    @Size(min = 1, max = 200, message = "내용을 입력해주세요.")
    private String content;

    @Schema(description = "댓글 작성자의 사용자 ID", example = "12345", required = true)
    private long userId;

    @Schema(description = "댓글이 작성된 게시물의 ID", example = "6789", required = true)
    private Long postId;

    @Schema(description = "댓글이 답글인 경우 부모 댓글의 ID", example = "9876", required = false)
    private Long parentId;
}
