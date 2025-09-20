package com.example.capstone.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewPostCreateRequest {
    @Schema(description = "제목", example = "우리 집 첫 입양 후기")
    @NotBlank
    private String title;

    @Schema(description = "본문", example = "처음 온 날부터 산책 연습했어요...")
    @NotBlank
    private String content;

    @NotBlank private String password;
}