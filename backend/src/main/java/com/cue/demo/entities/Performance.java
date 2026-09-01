package com.cue.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Builder @AllArgsConstructor
@Entity @Table(name="spectacol", indexes={
        @Index(name="idx_title", columnList="title, average_rating DESC"),
        @Index(name="idx_director", columnList="director, average_rating DESC"),
        @Index(name="location", columnList="location, average_rating DESC"),
        @Index(name="views", columnList="views_count DESC, average_rating DESC")
})
@SQLDelete(sql="UPDATE spectacol SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
@Getter
public class Performance {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String title;

    @Setter
    private String director;

    @Setter
    private String location;

    @Setter @Column(name="theater_name")
    private String theaterName;

    @Setter @Column(name="start_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime startDateTime;

    @Setter @Column(name="truncated_cover_image_URL")
    private String coverImageURL;

    @Setter @Column(name = "age_limit")
    private Integer ageLimit;

    @Setter @Column(name = "duration_minutes")
    private Integer duration;

    @Setter @Column(name="fullsize_cover_image_URL")
    private String fullCoverImageURL;

    @Setter @Column(name="purchase_ticket_link")
    private String purchaseTicketLink;

    @Setter @Column(columnDefinition = "TEXT")
    private String description;

    @Setter
    @JdbcTypeCode(SqlTypes.JSON) @Column(columnDefinition = "jsonb")
    private List<Credit> creditList;

    @OneToMany(mappedBy = "performance", cascade = CascadeType.ALL)
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

    @Builder.Default
    @Column(name="deleted")
    private boolean deleted = false;

    protected Performance() {}

}
