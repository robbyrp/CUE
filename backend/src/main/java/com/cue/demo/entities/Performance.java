package com.cue.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Builder @AllArgsConstructor
@Entity @Table(name="spectacol")
public class Performance {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String director;
    private String location;

    @Column(name="theater_name")
    private String theaterName;

    @Column(name="start_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime startDateTime;

    @Column(name="truncated_cover_image_URL")
    private String coverImageURL;

    @Column(name = "age_limit")
    private Integer ageLimit;

    @Column(name = "duration_minutes")
    private Integer duration;

    @Column(name="fullsize_cover_image_URL")
    private String fullCoverImageURL;

    @Column(name="purchase_ticket_link")
    private String purchaseTicketLink;

    private String description;

    @JdbcTypeCode(SqlTypes.JSON) @Column(columnDefinition = "jsonb")
    private List<Credit> creditList;

    @OneToMany(mappedBy = "performance", cascade = CascadeType.ALL)
    @Column(name="review_list")
    private List<Review> reviewList;

    @Builder.Default
    @Column(name="views_count")
    private Integer viewsCount = 0;

    @Builder.Default
    @Column(name="average_rating")
    private Double averageRating = 0.0;

    @CreationTimestamp
    @Column(name="created_at", updatable = false)
    private LocalDateTime createdDateTime;

    protected Performance() {}


}
