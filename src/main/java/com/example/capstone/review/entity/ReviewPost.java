package com.example.capstone.review.entity;

import com.example.capstone.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "review_posts", indexes = {
        @Index(name = "idx_review_title", columnList = "title"),
        @Index(name = "idx_review_createdAt", columnList = "createdAt")
})
public class ReviewPost extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 120, nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private long likeCount;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void increaseLike() {
        this.likeCount++;
    }
}