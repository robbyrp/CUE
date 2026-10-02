package com.cue.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table (name="review_heart",
        uniqueConstraints = {@UniqueConstraint(name="unique_review_heart_by_user_and_review",
        columnNames={"user_id", "review_id"})})
@Builder @AllArgsConstructor
@Getter
public class ReviewHeartItem {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="review_id", nullable=false)
    private Review review;

    @Builder.Default
    private boolean deleted = false;

    protected ReviewHeartItem() {}
}
