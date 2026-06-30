package com.cue.demo.services;

import com.cue.demo.dtos.PerformancePortalDTO;
import com.cue.demo.entities.Performance;
import com.cue.demo.exceptions.PerformanceAlreadyExistsException;
import com.cue.demo.repositories.PerformanceRepository;
import org.springframework.stereotype.Service;

@Service
public class PerformancePortalService {
    private PerformanceRepository performanceRepository;


    /**
     * Creates and saves the Performance Entity in the database after checking validity
     * Validation checks: checking for duplicates
     * Performances are identified by both title and director (At the moment)
     * @param performancePortalDTO The Performance Object received from the POST request
     */
    public void createPerformance(final PerformancePortalDTO performancePortalDTO) throws PerformanceAlreadyExistsException {
        checkNoDuplicate(performancePortalDTO);
        Performance performance = Performance.builder()
                .title(performancePortalDTO.title())
                .director(performancePortalDTO.director())
                .theaterName(performancePortalDTO.theaterName())
                .startDateTime(performancePortalDTO.startDateTime())
                .coverImageURL(performancePortalDTO.coverImageURL())
                .ageLimit(performancePortalDTO.ageLimit())
                .duration(performancePortalDTO.duration())
                .fullCoverImageURL(performancePortalDTO.fullCoverImageURL())
                .purchaseTicketLink(performancePortalDTO.purchaseTicketLink())
                .description(performancePortalDTO.description())
                .creditList(performancePortalDTO.credits())
                .reviewList(performancePortalDTO.reviews())
                .build();

        performanceRepository.save(performance);
    }

    public void checkNoDuplicate(final PerformancePortalDTO performancePortalDTO) throws PerformanceAlreadyExistsException {
        String title = performancePortalDTO.title();
        String director = performancePortalDTO.director();
        if (performanceRepository.existsByTitleAndDirector(title, director)) {
            throw new PerformanceAlreadyExistsException("Performance already exists in the database: criteria is title AND director");
        }
    }

}
