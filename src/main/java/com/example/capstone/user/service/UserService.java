package com.example.capstone.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.capstone.user.dto.SignupRequestDTO;
import com.example.capstone.user.dto.UserResponseDTO;
import com.example.capstone.user.entity.User;
import com.example.capstone.user.repository.UserRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원가입
    public UserResponseDTO register(SignupRequestDTO request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        User newUser = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .build();

        User savedUser = userRepository.save(newUser);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getPassword(),
                savedUser.getNickname()
        );
    }

    // 로그인
    public User login(String email, String password) {
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 이메일입니다."));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    // 사용자 정보 조회
    public UserResponseDTO getUserProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getNickname()
        );
    }

    // 사용자 정보 수정
    public User updateProfile(Long id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(updatedUser.getPassword());
        }

        if (updatedUser.getNickname() != null && !updatedUser.getNickname().isEmpty()) {
            user.setNickname(updatedUser.getNickname());
        }

        return user;
    }
}