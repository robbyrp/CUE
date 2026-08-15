package com.cue.demo.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table (name="review",
        indexes = {@Index(name = "idx_user_id", columnList = "user")},
        uniqueConstraints = {@UniqueConstraint(name = "unique_user_and_performance",
                columnNames = {"user_id", "performance_id"})})
@Builder @AllArgsConstructor
@Entity
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne @JoinColumn(name="performance_id")
    @JsonIgnore
    private Performance performance;

    @ManyToOne @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private Integer stars;
    private Integer hearts;

    @Column(columnDefinition = "TEXT")
    private String text;

    private boolean isSpoiler;

    /** Number of times it has been reported **/
    @Builder.Default
    private Integer reports = 0;

    protected Review() {}

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", user=" + user +
                ", createdAt=" + createdAt +
                ", text=" + text +
                '}';
    }
}
