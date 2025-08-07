package com.example.capstone.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupRequestDTO {
    private String email;
    private String password;
    private String confirmPassword;
    private String nickname;
}