package com.example.demo.likes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LikesResponseDTO {
    @Schema(description = "게시물의 좋아요 수", example = "120")
    private int likeCount;

    @Schema(description = "사용자가 게시물을 좋아요 했는지 여부", example = "true")
    private boolean isLiked;
}
