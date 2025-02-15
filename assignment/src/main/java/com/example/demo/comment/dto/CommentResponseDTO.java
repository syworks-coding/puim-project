package com.example.demo.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
public class CommentResponseDTO {
    @Schema(description = "댓글 목록", example = "[{\"id\": 1, \"content\": \"첫 번째 댓글\", \"userId\": 123}]")
    private List<CommentViewDTO> comments;

    @Schema(description = "댓글의 총 개수", example = "15")
    private int totalCount;
}
