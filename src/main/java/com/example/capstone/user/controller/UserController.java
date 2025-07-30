package com.example.capstone.user.controller;

import com.example.capstone.user.dto.UserSignupRequestDTO;
import com.example.capstone.user.entity.User;
import com.example.capstone.user.service.UserService;
import com.example.capstone.user.dto.LoginRequest;
import com.example.capstone.user.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/register")
    public String register(@RequestBody UserSignupRequestDTO signupRequest) {
        userService.register(signupRequest);
        return "회원가입 성공!";
    }

    // 프로필 수정
    @PutMapping("/{id}")
    public String updateProfile(@PathVariable Long id, @RequestBody User updatedUser) {
        userService.updateProfile(id, updatedUser);
        return "프로필 수정 성공!";
    }

    // 로그인
    @PostMapping("/login")
    public UserResponseDTO login(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getNickname()
        );
    }

    // 사용자 조회
    @GetMapping("/{id}")
    public UserResponseDTO getUserProfile(@PathVariable Long id) {
        return userService.getUserProfile(id);
    }
}