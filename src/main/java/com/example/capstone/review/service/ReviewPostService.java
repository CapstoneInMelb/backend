package com.example.capstone.review.service;

import com.example.capstone.review.dto.ReviewPostCreateRequest;
import com.example.capstone.review.dto.ReviewPostDto;
import com.example.capstone.review.dto.ReviewPostUpdateRequest;
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
public class ReviewPostService {

    private final ReviewPostRepository postRepository;
    private final ReviewCommentRepository commentRepository;

    @Transactional
    public ReviewPostDto create(ReviewPostCreateRequest req) {
        ReviewPost post = ReviewPost.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .likeCount(0L)
                .build();
        ReviewPost saved = postRepository.save(post);
        return toDto(saved, 0L);
    }

    @Transactional(readOnly = true)
    public Page<ReviewPostDto> list(String keyword, Pageable pageable) {
        Page<ReviewPost> page;
        if (keyword == null || keyword.isBlank()) {
            page = postRepository.findAll(pageable);
        } else {
            page = postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable);
        }
        return page.map(p -> toDto(p, commentRepository.countByPost(p)));
    }

    @Transactional(readOnly = true)
    public ReviewPostDto get(Long id) {
        ReviewPost post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다: " + id));
        long commentCnt = commentRepository.countByPost(post);
        return toDto(post, commentCnt);
    }

    @Transactional
    public ReviewPostDto update(Long id, ReviewPostUpdateRequest req) {
        ReviewPost post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다: " + id));
        post.update(req.getTitle(), req.getContent());
        long commentCnt = commentRepository.countByPost(post);
        return toDto(post, commentCnt);
    }

    @Transactional
    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    public long like(Long id) {
        ReviewPost post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다: " + id));
        post.increaseLike();
        return post.getLikeCount();
    }

    private ReviewPostDto toDto(ReviewPost p, long commentCount) {
        return ReviewPostDto.builder()
                .id(p.getId())
                .title(p.getTitle())
                .content(p.getContent())
                .likeCount(p.getLikeCount())
                .commentCount(commentCount)
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}