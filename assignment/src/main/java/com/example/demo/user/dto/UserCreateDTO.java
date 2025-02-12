package com.example.demo.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
public class UserCreateDTO {

    @Schema(description = "사용자 아이디", example = "user123", minLength = 2, maxLength = 16)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "아이디는 영문과 숫자만 입력 가능합니다.")
    @Size(min = 2, max = 16, message = "아이디는 2자 이상 16자 이하로 입력해주세요.")
    private String username;

    @Schema(description = "비밀번호", example = "password123", minLength = 2, maxLength = 20)
    @Size(min = 2, max = 20, message = "비밀번호는 2자 이상 20자 이하로 입력해주세요.")
    private String password;

    @Schema(description = "비밀번호 확인", example = "password123", minLength = 2, maxLength = 20)
    @Size(min = 2, max = 20, message = "비밀번호는 2자 이상 20자 이하로 입력해주세요.")
    private String passwordCheck;

    public boolean passwordMatch() {
        return password.equals(passwordCheck);
    }
}
