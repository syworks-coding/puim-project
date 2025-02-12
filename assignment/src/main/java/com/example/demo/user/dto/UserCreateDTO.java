package com.example.demo.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
public class UserCreateDTO {

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "아이디는 영문과 숫자만 입력 가능합니다.")
    @Size(min = 2, max = 16, message = "아이디는 2자 이상 16자 이하로 입력해주세요.")
    private String username;

    @Size(min = 2, max = 20, message = "비밀번호는 2자 이상 20자 이하로 입력해주세요.")
    private String password;

    @Size(min = 2, max = 20, message = "비밀번호는 2자 이상 20자 이하로 입력해주세요.")
    private String passwordCheck;

    public boolean passwordMatch() {
        return password.equals(passwordCheck);
    }
}
