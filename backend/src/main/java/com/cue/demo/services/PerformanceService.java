package com.cue.demo.services;

import com.cue.demo.dtos.PerformanceCardDTO;
import com.cue.demo.dtos.PerformanceDTO;
import com.cue.demo.entities.Performance;
import com.cue.demo.entities.User;
import com.cue.demo.exceptions.*;
import com.cue.demo.dtos.SearchSuggestion;
import com.cue.demo.mapper.PerformanceMapper;
import com.cue.demo.repositories.PerformanceRepository;
import com.cue.demo.repositories.UserRepository;
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
    private final UserRepository userRepository;
    private final PerformanceMapper mapper;

    /**
     * Creates and saves the Performance Entity in the database after checking validity.
     * The user MUST be an Admin, and there can be no performance duplicates.
     * Performances are identified by both title and director (At the moment).
     * @param userId Provided in the request header.
     * @param performanceDTO Performance Object received from the POST request.
     */
    @Transactional
    public void createPerformance(final Long userId, final PerformanceDTO performanceDTO)
            throws PerformanceAlreadyExistsException {

        verifyAdminStatus(userId);
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
     * Returns the performance with the id given as a parameter.
     * @param id The ID of the performance, given as a path variable in the URL.
     * @return DTO
     * @throws PerformanceNotFoundByIdException Throws it if the performance with the given ID does not exist in the database.
     */

    public PerformanceDTO getPerformanceById(final Long id)
            throws PerformanceNotFoundByIdException {
        Performance p =  performanceRepository.findById(id)
                .orElseThrow(() -> new PerformanceNotFoundByIdException(id));
        return mapper.fromPerformanceEntityToPerformanceDTO(p);
    }

    /**
     *
     * @param pageable The pagination and sorting information (page number, size, sort criteria).
     * @return Returns a dynamically sized page of PerformanceDTO's. Returns an empty array
     * if there are no performances in the database.
     */
    public Page<PerformanceDTO> getPerformances(Pageable pageable) {
        Page<Performance> performancePage = performanceRepository.findAll(pageable);
        return performancePage.map(mapper::fromPerformanceEntityToPerformanceDTO);
    }

    /**
     * Sets the "deleted" column of the performance to true.
     * @param userId Provided in the request header.
     * @param id ID of the performance to be deleted.
     * @throws PerformanceNotFoundByIdException Throws it if the performance with the given ID does not exist in the database.
     */
    @Transactional
    public void deletePerformanceById(final Long userId, final Long id)
            throws PerformanceNotFoundByIdException {
        verifyAdminStatus(userId);

        if (!performanceRepository.existsById(id)) {
            throw new PerformanceNotFoundByIdException(id);
        }

        performanceRepository.deleteById(id);
    }

    /**
     * Handles the PUT Request meant to update an existing Performance Entity.
     * @param userId Provided in the request header.
     * @param id ID of performance, given as a query param.
     * @param performanceDTO DTO containing the old fields and the fields to be updated.
     * @return Returns the newly updated Entity, mapped as a DTO.
     * @throws PerformanceNotFoundByIdException Throws it if the performance with the given ID does not exist in the database.
     */
    @Transactional
    public PerformanceDTO updatePerformanceById(final Long userId, final Long id,
                                                final PerformanceDTO performanceDTO)
            throws PerformanceIdMismatchException, PerformanceNotFoundByIdException {

        verifyAdminStatus(userId);

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
     *
     * @param title String the user has typed until the method was called
     * @return Returns the first 6 pairs of (title-id) title suggestions by relevance.
     */
    public List<SearchSuggestion> getSearchTitleSuggestions(String title) {
        if (title == null || title.trim().isEmpty()) {
            return List.of();
        }

        return performanceRepository.searchTitleCompletionSuggestions(title);
    }

    /**
     * Performs a global search across multiple textual fields (title, director, location) using
     * a single keyword. The search is case-insensitive and matches partial strings.
     * <p>
     * <b>Performance Note:</b> Although this JPQL query references Java entity fields,
     * the underlying PostgreSQL engine will automatically map them to the database columns
     * and use the defined B-Tree indexes (e.g., 'idx_title', 'idx_director', 'location')
     * to highly optimize the execution plan whenever possible.
     * </p>
     *
     * @param keyword  The text fragment to search for across the fields.
     * @param pageable The pagination and sorting information (page number, size, sort criteria).
     * @return A paginated list of {@link Performance} entities matching the given keyword.
     */
    public Page<PerformanceCardDTO> getSearchResults(Pageable pageable, String keyword) {
        return performanceRepository.searchProducts(keyword, pageable).map(mapper::fromPerformanceEntityToPerformanceCardDTO);
    }

    /**
     * Helper method.
     * Checks if the user corresponding to the userId
     * has the Admin Role.
     * @param userIdRequestHeader User ID received in the Request Header.
     * @throws UserNotFoundByIdException If a user with the specified ID is not found.
     * @throws UserNotAuthorizedException If the user is not an Admin.
     */
    private void verifyAdminStatus(Long userIdRequestHeader)
            throws UserNotFoundByIdException, UserNotAuthorizedException{
        User user = userRepository.findById(userIdRequestHeader)
                .orElseThrow(() -> new UserNotFoundByIdException(userIdRequestHeader));
        if (!user.isAdmin()) {
            throw new UserNotAuthorizedException(userIdRequestHeader);
        }
    }

    /**
     * Helper method
     * Checks if a performance already exists: performances are identified
     * by both title and director (At the moment).
     * @param performanceDTO The Performance Object received from the POST request.
     * @throws PerformanceAlreadyExistsException Throws it if the performance the admin
     * introduces already exists in the database.
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
