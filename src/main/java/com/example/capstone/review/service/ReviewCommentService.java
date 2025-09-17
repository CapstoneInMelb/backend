package com.example.capstone.review.service;


import com.example.capstone.review.dto.CommentCreateRequest;
import com.example.capstone.review.dto.CommentDto;
import com.example.capstone.review.dto.PageResponse;
import com.example.capstone.review.entity.ReviewComment;
import com.example.capstone.review.entity.ReviewPost;
import com.example.capstone.review.repository.ReviewCommentRepository;
import com.example.capstone.review.repository.ReviewPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewCommentService {

    private final ReviewPostRepository postRepository;
    private final ReviewCommentRepository commentRepository;

    @Transactional
    public CommentDto add(Long postId, CommentCreateRequest req) {
        ReviewPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다: " + postId));
        ReviewComment saved = commentRepository.save(
                ReviewComment.builder()
                        .post(post)
                        .content(req.getContent())
                        .build()
        );
        return toDto(saved);
    }

    @Transactional
    public void delete(Long postId, Long commentId) {
        // (간단 검증) 요청한 댓글이 해당 게시글 소속인지 확인
        ReviewComment c = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다: " + commentId));
        if (!c.getPost().getId().equals(postId)) {
            throw new IllegalArgumentException("댓글이 해당 게시글에 속하지 않습니다.");
        }
        commentRepository.delete(c);
    }

    @Transactional(readOnly = true)
    public PageResponse<CommentDto> list(Long postId, Pageable pageable) {
        ReviewPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다: " + postId));
        Page<CommentDto> page = commentRepository.findByPost(post, pageable).map(this::toDto);
        return PageResponse.from(page);
    }

    private CommentDto toDto(ReviewComment c) {
        return CommentDto.builder()
                .id(c.getId())
                .postId(c.getPost().getId())
                .content(c.getContent())
                .createdAt(c.getCreatedAt())
                .build();
    }
}