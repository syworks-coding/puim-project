package com.example.demo.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class CommentViewDTO {

    @Schema(description = "댓글 ID", example = "1")
    private Long id;

    @Schema(description = "댓글이 속한 게시물의 ID", example = "100")
    private Long postId;

    @Schema(description = "댓글 작성자의 사용자 ID", example = "12345")
    private Long userId;

    @Schema(description = "댓글 작성자의 사용자 이름", example = "홍길동")
    private String username;

    @Schema(description = "댓글 내용", example = "이것은 댓글 내용입니다.")
    private String content;

    @Schema(description = "댓글에 대한 답글 목록들")
    private List<CommentViewDTO> replies;

    @Schema(description = "댓글 작성 시간", example = "2025-02-12 14:30:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @Schema(description = "댓글 수정 시간", example = "2025-02-12 14:45:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    @Schema(description = "댓글 삭제 여부", example = "false")
    private boolean isDeleted;
}
