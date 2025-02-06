package com.example.demo.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO {

    @NotBlank
    @Size(max = 16)
    private String userId;

    @NotBlank
    @Size(max = 20)
    private String userPw;
}
