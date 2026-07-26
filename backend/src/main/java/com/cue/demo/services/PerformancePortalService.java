package com.cue.demo.services;

import com.cue.demo.dtos.PerformanceCardDTO;
import com.cue.demo.dtos.PerformancePortalDTO;
import com.cue.demo.entities.Performance;
import com.cue.demo.exceptions.PerformanceAlreadyExistsException;
import com.cue.demo.exceptions.PerformanceNotFoundByIdException;
import com.cue.demo.dtos.SearchSuggestion;
import com.cue.demo.repositories.PerformanceRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerformancePortalService {
    private final PerformanceRepository performanceRepository;

    /**
     * Creates and saves the Performance Entity in the database after checking validity.
     * Validation checks: checking for duplicates.
     * Performances are identified by both title and director (At the moment).
     * @param performancePortalDTO The Performance Object received from the POST request.
     */
    @Transactional
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
        Page<Performance> performancePage = performanceRepository.findAll(pageable);
        return performancePage.map(this::mapToPerformancePortalDTO);
    }

    /**
     * Sets the "deleted" column of the performance to true.
     * @param id ID of the performance to be deleted.
     * @throws PerformanceNotFoundByIdException Throws it if the performance with the given ID does not exist in the database.
     */
    @Transactional
    public void deletePerformanceById(final Long id) throws PerformanceNotFoundByIdException {
        if (!performanceRepository.existsById(id)) {
            throw new PerformanceNotFoundByIdException("Could not find performance with id: " + id);
        }

        performanceRepository.deleteById(id);
    }

    /**
     * Handles the PUT Request meant to update an existing Performance Entity.
     * @param id Id of performance, specified in URL.
     * @param performancePortalDTO DTO containing the old fields and the fields to be updated.
     * @return Returns the newly updated Entity, mapped as a DTO.
     * @throws PerformanceNotFoundByIdException Throws it if the performance with the given ID does not exist in the database.
     */
    @Transactional
    public PerformancePortalDTO updatePerformanceById(
            final Long id, final PerformancePortalDTO performancePortalDTO) throws PerformanceNotFoundByIdException {

        if (!Objects.equals(id, performancePortalDTO.id())) {
            throw new PerformanceNotFoundByIdException("ID FROM URL " + id + " IS IN CONFLICT WITH" +
                    " ID FROM REQUEST BODY: " + performancePortalDTO.id());
        }
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new PerformanceNotFoundByIdException("Could not find performance with id: " + id));

        performance.mapFromDTO(performancePortalDTO);
        performanceRepository.save(performance);
        return mapToPerformancePortalDTO(performance);
    }

    /**
     *
     * @param title String the user has typed until the method was called
     * @return Returns the first 6 pairs of (title-id) title suggestions by relevance.
     */
    public List<SearchSuggestion> getSearchTitleSuggestions(String title) {
        if (title == null || title.trim().isEmpty()) {
            return List.of();
        }

        return performanceRepository.findTop6ByTitleContainingIgnoreCase(title);
    }

    /**
     * Performs a global search across multiple textual fields (title, director, location) using
     * a single keyword. The search is case-insensitive and matches partial strings.
     * <p>
     * <b>Performance Note:</b> Although this JPQL query references Java entity fields,
     * the underlying PostgreSQL engine will automatically map them to the database columns
     * and utilize the defined B-Tree indexes (e.g., 'idx_title', 'idx_director', 'location')
     * to highly optimize the execution plan whenever possible.
     * </p>
     *
     * @param keyword  The text fragment to search for across the fields.
     * @param pageable The pagination and sorting information (page number, size, sort criteria).
     * @return A paginated list of {@link Performance} entities matching the given keyword.
     */
    public Page<PerformanceCardDTO> getSearchResults(Pageable pageable, String keyword) {
        return performanceRepository.searchProducts(keyword, pageable).map(this::mapToPerformanceCardDTO);
    }


    /**
     * Helper method
     * Checks if a performance already exists: performances are identified
     * by both title and director (At the moment).
     * @param performancePortalDTO The Performance Object received from the POST request.
     * @throws PerformanceAlreadyExistsException Throws it if the performance the admin
     * introduces already exists in the database.
     */
    private void checkNoDuplicate(final PerformancePortalDTO performancePortalDTO) throws PerformanceAlreadyExistsException {
        String title = performancePortalDTO.title();
        String director = performancePortalDTO.director();
        if (performanceRepository.existsByTitleAndDirector(title, director)) {
            throw new PerformanceAlreadyExistsException("Performance already exists in the database: criteria is title AND director");
        }
    }

    /**
     * Helper method.
     * Maps the Performance Entity to a PerformanceCardDTO object.
     * @param p The performance to be mapped.
     * @return Returns the newly mapped DTO object.
     */
    private PerformanceCardDTO mapToPerformanceCardDTO(final Performance p) {
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
