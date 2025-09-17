package com.example.capstone.review.repository;

import com.example.capstone.review.entity.ReviewPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewPostRepository extends JpaRepository<ReviewPost, Long> {
    Page<ReviewPost> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String t, String c, Pageable pageable);
}