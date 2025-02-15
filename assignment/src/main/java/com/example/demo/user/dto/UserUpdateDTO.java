package com.example.demo.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserUpdateDTO {

    @Schema(description = "현재 비밀번호", example = "currentPassword123", minLength = 2, maxLength = 16)
    @Size(min = 2, max = 16, message = "현재 비밀번호를 입력해주세요")
    private String oldPw;

    @Schema(description = "새 비밀번호", example = "newPassword123", minLength = 2, maxLength = 16)
    @Size(min = 2, max = 16, message = "새 비밀번호를 입력해주세요")
    private String newPw;

    @Schema(description = "새 비밀번호 확인", example = "newPassword123", minLength = 2, maxLength = 16)
    @Size(min = 2, max = 16, message = "새 비밀번호 확인을 입력해주세요")
    private String newPwCheck;

    public boolean idPasswordMatch() {
        return newPw.equals(newPwCheck);
    }
}
