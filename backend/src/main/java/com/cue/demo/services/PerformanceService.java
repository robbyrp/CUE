package com.cue.demo.services;

import com.cue.demo.dtos.performance.PerformanceCardDTO;
import com.cue.demo.dtos.performance.PerformanceDTO;
import com.cue.demo.entities.Performance;
import com.cue.demo.exceptions.*;
import com.cue.demo.dtos.search.SearchSuggestion;
import com.cue.demo.mapper.PerformanceMapper;
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
public class PerformanceService {
    private final PerformanceRepository performanceRepository;
    private final PerformanceMapper mapper;

    /**
     * Creates and saves the Performance Entity in the database after checking
     * validity. The user MUST be an Admin, and there can be no performance
     * duplicates. Performances are identified by both title and director.
     *
     * @param performanceDTO The DTO containing the performance data.
     * @throws PerformanceAlreadyExistsException If the performance already
     *                                           exists.
     */
    @Transactional
    public void createPerformance (final PerformanceDTO performanceDTO)
            throws PerformanceAlreadyExistsException {

        checkNoDuplicate(performanceDTO);

        Performance performance = Performance.builder()
                .title(performanceDTO.title())
                .director(performanceDTO.director())
                .location(performanceDTO.location())
                .theaterName(performanceDTO.theaterName())
                .startDateTime(performanceDTO.startDateTime())
                .coverImageURL(performanceDTO.coverImageURL())
                .ageLimit(performanceDTO.ageLimit())
                .duration(performanceDTO.duration())
                .fullCoverImageURL(performanceDTO.fullCoverImageURL())
                .purchaseTicketLink(performanceDTO.purchaseTicketLink())
                .description(performanceDTO.description())
                .creditList(performanceDTO.credits())
                .reviewList(performanceDTO.reviews())
                .build();

        performanceRepository.save(performance);
    }

    /**
     * Returns the performance with the ID given as a parameter.
     *
     * @param id The ID of the performance.
     * @return The performance DTO.
     * @throws PerformanceNotFoundByIdException If a performance with the
     *                                           specified ID is not found.
     */
    @Transactional
    public PerformanceDTO getPerformanceById(final Long id)
            throws PerformanceNotFoundByIdException {
        Performance p =  performanceRepository.findById(id)
                .orElseThrow(() -> new PerformanceNotFoundByIdException(id));
        p.increaseViewsCount();
        return mapper.fromPerformanceEntityToPerformanceDTO(p);
    }

    /**
     * Gets all performances from the database within a page.
     *
     * @param pageable The pagination and sorting information.
     * @return A page of performance DTOs.
     */
    public Page<PerformanceDTO> getPerformances(Pageable pageable) {
        Page<Performance> performancePage = performanceRepository.findAll(pageable);
        return performancePage.map(mapper::fromPerformanceEntityToPerformanceDTO);
    }

    /**
     * Deletes the performance with the specified ID.
     *
     * @param id The ID of the performance.
     * @throws PerformanceNotFoundByIdException If a performance with the
     *                                           specified ID is not found.
     */
    @Transactional
    public void deletePerformanceById(final Long id)
            throws PerformanceNotFoundByIdException {

        if (!performanceRepository.existsById(id)) {
            throw new PerformanceNotFoundByIdException(id);
        }

        performanceRepository.deleteById(id);
    }

    /**
     * Updates an existing performance entity.
     *
     * @param id The ID of the performance.
     * @param performanceDTO The DTO containing the performance data.
     * @return The updated performance DTO.
     * @throws PerformanceNotFoundByIdException If a performance with the
     *                                           specified ID is not found.
     * @throws PerformanceIdMismatchException If the provided ID does not match
     *                                         the ID in the DTO.
     */
    @Transactional
    public PerformanceDTO updatePerformanceById(final Long id,
                                                final PerformanceDTO performanceDTO)
            throws PerformanceIdMismatchException, PerformanceNotFoundByIdException {

        if (!Objects.equals(id, performanceDTO.id())) {
            throw new PerformanceIdMismatchException(id, performanceDTO.id());
        }
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new PerformanceNotFoundByIdException(id));

        mapper.updatePerformanceEntityFromPerformanceDTO(performanceDTO, performance);
        performanceRepository.save(performance);
        return mapper.fromPerformanceEntityToPerformanceDTO(performance);
    }

    /**
     * Performs a global search across the title field.
     *
     * @param title The title of the performance.
     * @return A list of search suggestions.
     */
    public List<SearchSuggestion> getSearchTitleSuggestions(String title) {
        if (title == null || title.trim().isEmpty()) {
            return List.of();
        }

        return performanceRepository.searchTitleCompletionSuggestions(title);
    }

    /**
     * Performs a global search across multiple textual fields (title, director,
     * location) using a single keyword. The search is case-insensitive and
     * matches partial strings.
     *
     * @param keyword The text fragment to search for across the fields.
     * @param pageable The pagination and sorting information.
     * @return A paginated list of performance card DTOs.
     */
    public Page<PerformanceCardDTO> getSearchResults(Pageable pageable, String keyword) {
        return performanceRepository.searchProducts(keyword, pageable).map(mapper::fromPerformanceEntityToPerformanceCardDTO);
    }

    /**
     * Checks if a performance already exists by title and director.
     *
     * @param performanceDTO The DTO containing the performance data.
     * @throws PerformanceAlreadyExistsException If the performance already
     *                                           exists.
     */
    private void checkNoDuplicate(final PerformanceDTO performanceDTO)
            throws PerformanceAlreadyExistsException {
        String title = performanceDTO.title();
        String director = performanceDTO.director();
        if (performanceRepository.existsByTitleAndDirector(title, director)) {
            throw new PerformanceAlreadyExistsException(title, director);
        }
    }

}
