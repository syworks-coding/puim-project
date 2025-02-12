package com.example.demo.post.dto;

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

    @Size(min = 1, max = 20, message = "제목을 입력해주세요.")
    private String title;

    @Size(min = 1, max = 1500, message = "내용은 최소 10자 이상 작성해주세요.")
    private String content;

    private long userId;
}
