package com.example.capstone.review.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateRequest {
    @Schema(description = "댓글 본문", example = "너무 감동이에요! 산책 코스 추천 드려요.")
    @NotBlank
    private String content;
}