package com.example.demo.post.dto;

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

    @NotNull
    private long id;

    @NotNull
    private long userId;

    @Size(min = 1, max = 40, message = "제목을 입력해주세요.")
    private String title;

    @Size(min = 10, max = 1500, message = "내용은 최소 10자 이상 작성해주세요.")
    private String content;
}
