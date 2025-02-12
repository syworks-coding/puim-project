package com.example.demo.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostUpdateDTO {

    @Schema(description = "게시물 ID", example = "1")
    @NotNull
    private long id;

    @Schema(description = "사용자 ID", example = "1")
    @NotNull
    private long userId;

    @Schema(description = "게시물 제목", example = "Spring Boot 게시물 업데이트", minLength = 1, maxLength = 40)
    @Size(min = 1, max = 40, message = "제목을 입력해주세요.")
    private String title;

    @Schema(description = "게시물 내용", example = "게시물 내용을 업데이트하는 방법에 대해 설명합니다.", minLength = 10, maxLength = 1500)
    @Size(min = 10, max = 1500, message = "내용은 최소 10자 이상 작성해주세요.")
    private String content;
}
