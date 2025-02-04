package com.example.demo.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO {

    @NotBlank
    private String userId;

    @NotBlank
    private String userPw;
}
