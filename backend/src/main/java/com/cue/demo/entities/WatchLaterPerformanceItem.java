package com.cue.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table (name="watch_later_item",
        indexes = {@Index(name = "idx_user_id", columnList = "user_id")},
        uniqueConstraints = {@UniqueConstraint(name = "unique_user_and_performance",
                          columnNames = {"user_id", "performance_id"})})
@Getter @Builder @AllArgsConstructor
@SQLDelete(sql = "UPDATE watch_later_item SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")

public class WatchLaterPerformanceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="performance_id", nullable = false)
    private Performance performance;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime addedAtTime;

    @Builder.Default
    private boolean deleted = false;

    protected WatchLaterPerformanceItem() {}
}
