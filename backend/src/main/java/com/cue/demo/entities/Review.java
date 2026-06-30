package com.cue.demo.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @CreationTimestamp
    @Column(name="publishing_date")
    private LocalDateTime publishingDate;

    private Integer stars;
    private Integer hearts;

    private Integer text;

    @Column(name="is_spoiler")
    private boolean isSpoiler;

    /** Number of times it has been reported **/
    private Integer reports;

    @ManyToOne
    @JoinColumn(name="performance_id")
    private Performance performance;

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", user=" + user +
                ", publishingDate=" + publishingDate +
                ", text=" + text +
                '}';
    }
}
