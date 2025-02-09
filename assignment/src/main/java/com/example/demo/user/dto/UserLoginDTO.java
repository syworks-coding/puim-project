package com.example.demo.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserLoginDTO {
    @NotNull
    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;

    @NotNull
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
