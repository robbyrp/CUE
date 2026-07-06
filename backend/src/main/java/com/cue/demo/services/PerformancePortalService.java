package com.cue.demo.services;

import com.cue.demo.dtos.PerformancePortalDTO;
import com.cue.demo.entities.Performance;
import com.cue.demo.exceptions.PerformanceAlreadyExistsException;
import com.cue.demo.exceptions.PerformanceNotFoundByIdException;
import com.cue.demo.repositories.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerformancePortalService {
    private final PerformanceRepository performanceRepository;

    /**
     * Creates and saves the Performance Entity in the database after checking validity.
     * Validation checks: checking for duplicates.
     * Performances are identified by both title and director (At the moment).
     * @param performancePortalDTO The Performance Object received from the POST request.
     */
    public void createPerformance(final PerformancePortalDTO performancePortalDTO) throws PerformanceAlreadyExistsException {
        checkNoDuplicate(performancePortalDTO);
        Performance performance = Performance.builder()
                .title(performancePortalDTO.title())
                .director(performancePortalDTO.director())
                .location(performancePortalDTO.location())
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

    /**
     * Checks if a performance already exists: performances are identified
     * by both title and director (At the moment).
     * @param performancePortalDTO The Performance Object received from the POST request.
     * @throws PerformanceAlreadyExistsException Throws it if the performance the admin
     * introduces already exists in the database.
     */
    public void checkNoDuplicate(final PerformancePortalDTO performancePortalDTO) throws PerformanceAlreadyExistsException {
        String title = performancePortalDTO.title();
        String director = performancePortalDTO.director();
        if (performanceRepository.existsByTitleAndDirector(title, director)) {
            throw new PerformanceAlreadyExistsException("Performance already exists in the database: criteria is title AND director");
        }
    }

    /**
     * Returns the performance with the id given as a parameter.
     * @param id The ID of the performance, given as a path variable in the URL.
     * @return DTO
     * @throws PerformanceNotFoundByIdException Throws it if the performance with the given ID does not exist in the database.
     */
    public PerformancePortalDTO getPerformanceById(final Long id) throws PerformanceNotFoundByIdException {
        Performance p =  performanceRepository.findById(id)
                .orElseThrow(() -> new PerformanceNotFoundByIdException("Performance not found by id: " + id));
        return mapToPerformancePortalDTO(p);
    }

    /**
     *
     * @param pageable Pageable object received as a path variable in the URL.
     * @return Returns a dynamically sized page of PerformancePortalDTO's. Returns an empty array
     * if there are no performances in the database.
     */
    public Page<PerformancePortalDTO> getPerformances(Pageable pageable) {
        Page<Performance> performancePage = performanceRepository.findByDeletedFalse(pageable);
        return performancePage.map(this::mapToPerformancePortalDTO);
    }

    /**
     * Sets the "deleted" column of the performance to true.
     * @param id ID of the performance to be deleted.
     * @throws PerformanceNotFoundByIdException Throws it if the performance with the given ID does not exist in the database.
     */
    public void deletePerformanceById(final Long id) throws PerformanceNotFoundByIdException {
        if (!performanceRepository.existsById(id)) {
            throw new PerformanceNotFoundByIdException("Could not find performance with id: " + id);
        }

        performanceRepository.deleteById(id);
    }

    /**
     * Handles the PUT Request meant to update an existing Performance Entity.
     * @param id Performance identified by id.
     * @param performancePortalDTO DTO containing the old fields and the fields to be updated.
     * @return Returns the newly updated Entity, mapped as a DTO.
     * @throws PerformanceNotFoundByIdException Throws it if the performance with the given ID does not exist in the database.
     */
    public PerformancePortalDTO updatePerformanceById(
            final Long id, final PerformancePortalDTO performancePortalDTO) throws PerformanceNotFoundByIdException {
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new PerformanceNotFoundByIdException("Could not find performance with id: " + id));

        performance.mapFromDTO(performancePortalDTO);
        performanceRepository.save(performance);
        return mapToPerformancePortalDTO(performance);
    }

    /**
     * Helper method.
     * Maps the Performance Entity to a PerformancePortalDTO object.
     * @param p The performance to be mapped.
     * @return Returns the newly mapped DTO object.
     */
    private PerformancePortalDTO mapToPerformancePortalDTO(final Performance p) {
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
