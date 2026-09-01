package com.cue.demo.services;

import com.cue.demo.dtos.performance.PerformanceCardDTO;
import com.cue.demo.dtos.performance.WatchPerformanceItemDTO;
import com.cue.demo.entities.*;
import com.cue.demo.exceptions.PerformanceNotFoundByIdException;
import com.cue.demo.exceptions.PerformanceNotInUsersWatchListException;
import com.cue.demo.exceptions.PerformanceNotInUsersWatchedListException;
import com.cue.demo.mapper.PerformanceMapper;
import com.cue.demo.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class UserService {
    private final PerformanceRepository performanceRepository;
    private final UserRepository userRepository;
    private final WatchLaterPerformanceItemRepository watchLaterRepository;
    private final WatchedPerformanceItemRepository watchedPerformanceRepository;
    private final PerformanceMapper mapper;
    private final ReviewRepository reviewRepository;

    /**
     * Adds a performance to the user's "watch later" list based on the data.
     *
     * @param dto The DTO containing the user ID and performance ID.
     * @throws PerformanceNotFoundByIdException If a performance with the
     *                                           specified ID is not found.
     */
    @Transactional
    public void addItemToWatchLater(final WatchPerformanceItemDTO dto)
            throws PerformanceNotFoundByIdException {

        User userProxy = userRepository.getReferenceById(dto.userId());

        Performance performance = performanceRepository.findById(dto.performanceId())
                .orElseThrow(() -> new PerformanceNotFoundByIdException(dto.performanceId()));

        if (!watchLaterRepository.existsByUserIdAndPerformanceId(dto.userId(), performance.getId())) {
            WatchLaterPerformanceItem item = WatchLaterPerformanceItem.builder()
                    .user(userProxy)
                    .performance(performance)
                    .build();
            watchLaterRepository.save(item);
        }
        //TODO: Add an else case, and modify the function return type so that it returns text that says: The performance is already in user's watch later list.
    }

    /**
     * Removes a performance from the user's "watch later" list.
     *
     * @param dto The DTO containing the user ID and performance ID.
     * @throws PerformanceNotFoundByIdException If a performance with the
     *                                           specified ID is not found.
     * @throws PerformanceNotInUsersWatchListException If the performance is not
     *                                                  in the watch later list.
     */
    @Transactional
    public void deleteItemFromWatchLater(final WatchPerformanceItemDTO dto)
            throws PerformanceNotFoundByIdException {

        Performance performance = performanceRepository.findById(dto.performanceId())
                .orElseThrow(() -> new PerformanceNotFoundByIdException(dto.performanceId()));

        if (watchLaterRepository.existsByUserIdAndPerformanceId(dto.userId(), performance.getId())) {
            watchLaterRepository.deleteByUserIdAndPerformanceId(dto.userId(), performance.getId());
        } else {
            throw new PerformanceNotInUsersWatchListException(dto.userId(), performance.getId());
        }
    }

    /**
     * Gets a page of PerformanceCardDTOs from the user's "watch later" list.
     *
     * @param userId The ID of a valid user, filtered before reaching the
     *                controller.
     * @param pageable The pagination and sorting information.
     * @return A page of performance card DTOs.
     */
    public Page<PerformanceCardDTO> getWatchLaterPerformanceCards(final Long userId, final Pageable pageable) {

        Page<WatchLaterPerformanceItem> watchLaterPage = watchLaterRepository.findByUserId(userId, pageable);
        return watchLaterPage.map(mapper::fromWatchLaterItemEntityToPerformanceCardDTO);
    }

    /**
     * Returns true if performance with id is in the user's watch later list.
     *
     * @param userId The ID of a valid user, filtered before reaching the
     *                controller.
     * @param performanceId The ID of the performance.
     * @return True if the performance is in the list, false otherwise.
     * @throws PerformanceNotFoundByIdException If a performance with the
     *                                           specified ID is not found.
     */
    public Boolean isPerformanceInWatchLater(final Long userId, final Long performanceId)
            throws PerformanceNotFoundByIdException {

        if (!performanceRepository.existsById(performanceId)) {
            throw new PerformanceNotFoundByIdException(performanceId);
        }

        return watchLaterRepository.existsByUserIdAndPerformanceId(userId, performanceId);
    }

    /**
     * Adds a performance to the user's "watched" list based on the provided data.
     *
     * @param dto The DTO containing the user ID and performance ID.
     * @throws PerformanceNotFoundByIdException If a performance with the
     *                                           specified ID is not found.
     */
    @Transactional
    public void addItemToWatched(final WatchPerformanceItemDTO dto)
            throws PerformanceNotFoundByIdException {

        User userProxy = userRepository.getReferenceById(dto.userId());

        Performance performance = performanceRepository.findById(dto.performanceId())
                .orElseThrow(() -> new PerformanceNotFoundByIdException(dto.performanceId()));

        if (!watchedPerformanceRepository.existsByUserIdAndPerformanceId(dto.userId(), performance.getId())) {
            WatchedPerformanceItem item = WatchedPerformanceItem.builder()
                    .user(userProxy)
                    .performance(performance)
                    .build();
            watchedPerformanceRepository.save(item);
        }
        //TODO: Add an else case, and modify the function return type so that it returns text that says: The performance is already in user's watched list.

    }

    /**
     * Removes a performance from the user's "watched" list.
     *
     * @param dto The DTO containing the user ID and performance ID.
     * @throws PerformanceNotFoundByIdException If a performance with the
     *                                           specified ID is not found.
     * @throws PerformanceNotInUsersWatchedListException If the performance is not
     *                                                    in the watched list.
     */
    @Transactional
    public void deleteItemFromWatched(final WatchPerformanceItemDTO dto)
            throws PerformanceNotFoundByIdException {

        Performance performance = performanceRepository.findById(dto.performanceId())
                .orElseThrow(() -> new PerformanceNotFoundByIdException(dto.performanceId()));

        if (watchedPerformanceRepository.existsByUserIdAndPerformanceId(dto.userId(), performance.getId())) {
            watchedPerformanceRepository.deleteByUserIdAndPerformanceId(dto.userId(), performance.getId());
        } else {
            throw new PerformanceNotInUsersWatchedListException(dto.userId(), performance.getId());
        }
    }

    /**
     * Gets a page of PerformanceCardDTOs from the user's "watched" list.
     *
     * @param userId The ID of a valid user, filtered before reaching the
     *                controller.
     * @param pageable The pagination and sorting information.
     * @return A page of performance card DTOs.
     */
    public Page<PerformanceCardDTO> getWatchedPerformanceCards(final Long userId, final Pageable pageable) {

        Page<WatchedPerformanceItem> watchedPage = watchedPerformanceRepository.findByUserId(userId, pageable);
        return watchedPage.map(mapper::fromWatchedItemEntityToPerformanceCardDTO);
    }

    /**
     * Returns true if performance with id is in the user's watched list.
     *
     * @param userId The ID of a valid user, filtered before reaching the
     *                controller.
     * @param performanceId The ID of the performance.
     * @return True if the performance is in the list, false otherwise.
     * @throws PerformanceNotFoundByIdException If a performance with the
     *                                           specified ID is not found.
     */
    public Boolean isPerformanceInWatched(final Long userId, final Long performanceId)
            throws PerformanceNotFoundByIdException {

        if (!performanceRepository.existsById(performanceId)) {
            throw new PerformanceNotFoundByIdException(performanceId);
        }

        return watchedPerformanceRepository.existsByUserIdAndPerformanceId(userId, performanceId);
    }

    /**
     * Gets the list of performances reviewed by a user.
     *
     * @param userId The ID of a valid user, filtered before reaching the
     *                controller.
     * @param pageable The pagination and sorting information.
     * @return A page of performance card DTOs.
     */
    public Page<PerformanceCardDTO> getReviewedPerformanceCards(final Long userId, final Pageable pageable) {

        Page<Performance> reviewedPerformances = reviewRepository.findPerformancesReviewedByUserId(userId, pageable);
        return reviewedPerformances.map(mapper::fromPerformanceEntityToPerformanceCardDTO);
    }
}
