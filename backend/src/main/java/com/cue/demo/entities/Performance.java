package com.cue.demo.entities;

import com.cue.demo.dtos.PerformancePortalDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
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

    private String title;
    private String director;
    private String location;

    @Column(name="theater_name")
    private String theaterName;

    @Column(name="start_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime startDateTime;

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

    @Builder.Default
    @Column(name="deleted")
    private boolean deleted = false;

    protected Performance() {}

    /**
     * Method that maps from a DTO to a Performance Entity.
     * Used to "update" performances in the database.
     * IMPORTANT: Does not inherit the reviews from the DTO.
     * createdAt, viewsCount, averageRating, and deleted are omitted as well,
     * as they are not present in the DTO.
     * @param dto The DTO from which it updates.
     */
    public void mapFromDTO(final PerformancePortalDTO dto) {
        this.title = dto.title();
        this.director = dto.director();
        this.location = dto.location();
        this.theaterName = dto.theaterName();
        this.startDateTime = dto.startDateTime();
        this.coverImageURL = dto.coverImageURL();
        this.ageLimit = dto.ageLimit();
        this.duration = dto.duration();
        this.fullCoverImageURL = dto.fullCoverImageURL();
        this.purchaseTicketLink = dto.purchaseTicketLink();
        this.description = dto.description();
        this.creditList = dto.credits();
    }


}
