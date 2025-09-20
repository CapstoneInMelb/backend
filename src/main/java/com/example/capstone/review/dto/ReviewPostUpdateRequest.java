package com.example.capstone.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewPostUpdateRequest {
    @Schema(description = "제목", example = "입양 1개월차 업데이트")
    @NotBlank
    private String title;

    @Schema(description = "본문", example = "한 달 동안 같이 지내보니...")
    @NotBlank
    private String content;

    @NotBlank private String password;
}