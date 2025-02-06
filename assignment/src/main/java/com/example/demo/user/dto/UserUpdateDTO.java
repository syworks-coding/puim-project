package com.example.demo.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserUpdateDTO {

    @NotNull
    @NotBlank
    @Size(min = 2, max = 16, message = "현재 비밀번호를 입력해주세요")
    private String oldPw;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 16, message = "새 비밀번호를 입력해주세요")
    private String newPw;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 16, message = "새 비밀번호 확인을 입력해주세요")
    private String newPwCheck;

    public boolean idPasswordMatch() {
        return newPw.equals(newPwCheck);
    }

    public boolean idPasswordEqualsCurrent() {
        return oldPw.equals(newPwCheck);
    }
}
