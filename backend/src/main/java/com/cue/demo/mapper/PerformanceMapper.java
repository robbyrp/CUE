package com.cue.demo.mapper;

import com.cue.demo.dtos.performance.PerformanceCardDTO;
import com.cue.demo.dtos.performance.PerformanceDTO;
import com.cue.demo.entities.Performance;
import com.cue.demo.entities.WatchLaterPerformanceItem;
import com.cue.demo.entities.WatchedPerformanceItem;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public final class PerformanceMapper {

    /**
     * Internal helper method to map a Performance entity to a PerformanceCardDTO.
     * @param p The performance entity to be mapped.
     * @return Returns the newly mapped PerformanceCardDTO object.
     */
    private PerformanceCardDTO mapToPerformanceCardDTO(final Performance p) {
        return PerformanceCardDTO.builder()
                .id(p.getId())
                .title(p.getTitle())
                .director(p.getDirector())
                .coverImageURL(p.getCoverImageURL())
                .ageLimit(p.getAgeLimit())
                .duration(p.getDuration())
                .build();
    }

    /**
     * Maps the PerformanceWatchLater Entity to a PerformanceCardDTO object
     * @param item the entity to be mapped
     * @return Returns the newly mapped DTO object.
     */
    public PerformanceCardDTO fromWatchLaterItemEntityToPerformanceCardDTO(final WatchLaterPerformanceItem item) {
        Performance p = item.getPerformance();
        return mapToPerformanceCardDTO(p);
    }

    /**
     * Maps the WatchedPerformanceItem Entity to a PerformanceCardDTO object
     * @param item the entity to be mapped
     * @return Returns the newly mapped DTO object.
     */
    public PerformanceCardDTO fromWatchedItemEntityToPerformanceCardDTO(final WatchedPerformanceItem item) {
        Performance p = item.getPerformance();
        return mapToPerformanceCardDTO(p);
    }

    /**
     * Maps the Performance Entity to a PerformanceCardDTO object.
     * @param p The performance to be mapped.
     * @return Returns the newly mapped DTO object.
     */
    public PerformanceCardDTO fromPerformanceEntityToPerformanceCardDTO(final Performance p) {
        return mapToPerformanceCardDTO(p);
    }


    /**
     * Maps the Performance Entity to a PerformanceDTO object.
     * @param p The performance to be mapped.
     * @return Returns the newly mapped DTO object.
     */
    public PerformanceDTO fromPerformanceEntityToPerformanceDTO(final Performance p) {

        return PerformanceDTO.builder()
                .id(p.getId())
                .title(p.getTitle())
                .director(p.getDirector())
                .location(p.getLocation())
                .coverImageURL(p.getCoverImageURL())
                .theaterName(p.getTheaterName())
                .startDateTime(p.getStartDateTime())
                .ageLimit(p.getAgeLimit())
                .duration(p.getDuration())
                .fullCoverImageURL(p.getFullCoverImageURL())
                .purchaseTicketLink(p.getPurchaseTicketLink())
                .description(p.getDescription())
                .credits(p.getCreditList())
                .build();
    }

    /**
     * Method that updates a Performance Entity in-place using setters.
     * Used to "update" performances in the database.
     * IMPORTANT: Does not inherit the reviews from the DTO.
     * createdAt, viewsCount, averageRating, and deleted are omitted as well,
     * as they are not present in the DTO.
     * @param dto The DTO from which it updates.
     * @param p the Performance entity which is updated in-place with setters.
     */
    public void updatePerformanceEntityFromPerformanceDTO(final PerformanceDTO dto, final Performance p) {
        if (dto == null || p == null) return;

        if (!Objects.equals(p.getTitle(), dto.title())) {
            p.setTitle(dto.title());
        }

        if (!Objects.equals(p.getDirector(), dto.director())) {
            p.setDirector(dto.director());
        }

        if (!Objects.equals(p.getLocation(), dto.location())) {
            p.setLocation(dto.location());
        }

        if (!Objects.equals(p.getTheaterName(), dto.theaterName())) {
            p.setTheaterName(dto.theaterName());
        }

        if (!Objects.equals(p.getStartDateTime(), dto.startDateTime())) {
            p.setStartDateTime(dto.startDateTime());
        }

        if (!Objects.equals(p.getCoverImageURL(), dto.coverImageURL())) {
            p.setCoverImageURL(dto.coverImageURL());
        }

        if (!Objects.equals(p.getAgeLimit(), dto.ageLimit())) {
            p.setAgeLimit(dto.ageLimit());
        }

        if (!Objects.equals(p.getDuration(), dto.duration())) {
            p.setDuration(dto.duration());
        }

        if (!Objects.equals(p.getFullCoverImageURL(), dto.fullCoverImageURL())) {
            p.setFullCoverImageURL(dto.fullCoverImageURL());
        }

        if (!Objects.equals(p.getPurchaseTicketLink(), dto.purchaseTicketLink())) {
            p.setPurchaseTicketLink(dto.purchaseTicketLink());
        }

        if (!Objects.equals(p.getDescription(), dto.description())) {
            p.setDescription(dto.description());
        }

        if (!Objects.equals(p.getCreditList(), dto.credits())) {
            p.setCreditList(dto.credits());
        }

    }
}
