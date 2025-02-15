package com.example.demo.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
// 저장용 DTO
public class PostCreateDTO {

    @Schema(description = "게시글 제목", example = "Spring Boot 사용법", minLength = 1, maxLength = 20)
    @Size(min = 1, max = 20, message = "제목을 입력해주세요.")
    private String title;

    @Schema(description = "게시글 내용", example = "Spring Boot를 사용해 웹 애플리케이션을 만드는 방법에 대해 설명합니다.", minLength = 1, maxLength = 80)
    @Size(min = 1, max = 80, message = "내용은 최소 10자 이상 작성해주세요.")
    private String content;

    @Schema(description = "작성자 ID", example = "1")
    private long userId;
}