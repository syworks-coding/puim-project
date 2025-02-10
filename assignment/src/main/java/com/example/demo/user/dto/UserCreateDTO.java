package com.example.demo.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
public class UserCreateDTO {

    @NotNull
    @NotBlank
    @Size(min = 2, max = 16, message = "아이디는 2자 이상 16자 이하로 입력해주세요.")
    private String username;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 20, message = "비밀번호는 2자 이상 20자 이하로 입력해주세요.")
    private String password;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 20, message = "비밀번호는 2자 이상 20자 이하로 입력해주세요.")
    private String passwordCheck;

    public boolean passwordMatch() {
        return password.equals(passwordCheck);
    }
}
