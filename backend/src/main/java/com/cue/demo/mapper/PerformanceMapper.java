package com.cue.demo.mapper;

import com.cue.demo.dtos.PerformanceCardDTO;
import com.cue.demo.dtos.PerformancePortalDTO;
import com.cue.demo.entities.Performance;
import com.cue.demo.entities.WatchLaterPerformanceItem;
import org.springframework.stereotype.Component;

@Component
public final class PerformanceMapper {

    /**
     * Maps the PerformanceWatchLater Entity to a PerformanceCardDTO object
     * @param item the entity to be mapped
     * @return Returns the newly mapped DTO object.
     */
    public PerformanceCardDTO fromWatchLaterItemEntityToPerformanceCardDTO(final WatchLaterPerformanceItem item) {
        Performance p = item.getPerformance();
        return fromPerformanceEntityToPerformanceCardDTO(p);
    }

    /**
     * Maps the Performance Entity to a PerformanceCardDTO object.
     * @param p The performance to be mapped.
     * @return Returns the newly mapped DTO object.
     */
    public PerformanceCardDTO fromPerformanceEntityToPerformanceCardDTO(final Performance p) {
        return PerformanceCardDTO.builder()
                .id(p.getId())
                .title(p.getTitle())
                .director(p.getDirector())
                .coverImageUrl(p.getCoverImageURL())
                .ageLimit(p.getAgeLimit())
                .duration(p.getDuration())
                .build();
    }

    /**
     * Maps the Performance Entity to a PerformancePortalDTO object.
     * @param p The performance to be mapped.
     * @return Returns the newly mapped DTO object.
     */
    public PerformancePortalDTO fromPerformanceEntityToPerformancePortalDTO(final Performance p) {
        return PerformancePortalDTO.builder()
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
                .reviews(p.getReviewList())
                .build();
    }
}
