package com.cue.demo.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import java.time.LocalDateTime;

@Table (name="review",
        indexes = {@Index(name = "idx_user_id", columnList = "user_id")},
        uniqueConstraints = {@UniqueConstraint(name = "unique_review_by_user_and_performance",
                columnNames = {"user_id", "performance_id"})})
@Builder @AllArgsConstructor
@Getter @Entity
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

    @Setter
    private Integer stars;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String text;

    @Setter
    private boolean isSpoiler;

    /** Number of times it has been reported **/
    @Setter
    @Builder.Default
    private Integer reports = 0;

    @Builder.Default
    @Formula("(SELECT COUNT(*) FROM review_heart rh WHERE rh.review_id = id)")
    private int hearts = 0;

    protected Review() {}

    /**
     * Checks if the User (author) of the review is
     * the same as the userId parameter.
     * @param userIdRequestHeader User ID received in the Request Header.
     * @return True if the review has an owner and if its id coincides with the parameter id.
     */
    public boolean isCreatedBy(Long userIdRequestHeader) {
        return this.user != null && this.getUser().getId().equals(userIdRequestHeader);
    }

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
