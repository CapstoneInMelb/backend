package com.example.capstone.review.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewPostDto {
    private Long id;
    private String title;
    private String content;
    private long likeCount;
    private long commentCount;
    @Schema(example = "2025-09-15T10:12:30")
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}