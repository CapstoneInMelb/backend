package com.example.capstone.review.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private Long postId;
    private String content;
    private LocalDateTime createdAt;
}