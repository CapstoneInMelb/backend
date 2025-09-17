package com.example.capstone.review.controller;

import com.example.capstone.review.dto.*;
import com.example.capstone.review.service.ReviewCommentService;
import com.example.capstone.review.service.ReviewPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "adoption-review", description = "입양 후기 게시판 API")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewPostService postService;
    private final ReviewCommentService commentService;

    // ========== 게시글 ==========
    @Operation(summary = "후기 작성")
    @PostMapping
    public ResponseEntity<ReviewPostDto> create(@Valid @RequestBody ReviewPostCreateRequest req) {
        return ResponseEntity.ok(postService.create(req));
    }

    @Operation(summary = "후기 목록 조회", description = "키워드(제목/본문) 검색, 페이지네이션, 정렬 지원. sort 예) createdAt,desc / likeCount,desc")
    @GetMapping
    public ResponseEntity<PageResponse<ReviewPostDto>> list(
            @Parameter(description = "검색 키워드") @RequestParam(required = false) String keyword,
            @Parameter(description = "페이지(0부터)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "사이즈") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "정렬 (필드,방향)") @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        String[] s = sort.split(",");
        Sort.Direction dir = (s.length > 1 && "asc".equalsIgnoreCase(s[1])) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, s[0]));
        var result = postService.list(keyword, pageable);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @Operation(summary = "후기 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ReviewPostDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(postService.get(id));
    }

    @Operation(summary = "후기 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ReviewPostDto> update(@PathVariable Long id,
                                                @Valid @RequestBody ReviewPostUpdateRequest req) {
        return ResponseEntity.ok(postService.update(id, req));
    }

    @Operation(summary = "후기 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "좋아요 +1")
    @PostMapping("/{id}/like")
    public ResponseEntity<Long> like(@PathVariable Long id) {
        long likeCount = postService.like(id);
        return ResponseEntity.ok(likeCount);
    }

    // ========== 댓글 ==========
    @Operation(summary = "댓글 작성")
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long postId,
                                                 @Valid @RequestBody CommentCreateRequest req) {
        return ResponseEntity.ok(commentService.add(postId, req));
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.delete(postId, commentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "댓글 목록 조회")
    @GetMapping("/{postId}/comments")
    public ResponseEntity<PageResponse<CommentDto>> getComments(
            @PathVariable Long postId,
            @Parameter(description = "페이지(0부터)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "사이즈") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(commentService.list(postId, pageable));
    }
}