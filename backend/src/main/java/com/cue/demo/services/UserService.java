package com.cue.demo.services;

import com.cue.demo.dtos.PerformanceCardDTO;
import com.cue.demo.dtos.WatchPerformanceItemDTO;
import com.cue.demo.entities.Performance;
import com.cue.demo.entities.User;
import com.cue.demo.entities.WatchLaterPerformanceItem;
import com.cue.demo.entities.WatchedPerformanceItem;
import com.cue.demo.exceptions.PerformanceNotFoundByIdException;
import com.cue.demo.exceptions.PerformanceNotInUsersWatchListException;
import com.cue.demo.exceptions.PerformanceNotInUsersWatchedListException;
import com.cue.demo.exceptions.UserNotFoundByIdException;
import com.cue.demo.mapper.PerformanceMapper;
import com.cue.demo.repositories.PerformanceRepository;
import com.cue.demo.repositories.UserRepository;
import com.cue.demo.repositories.WatchLaterPerformanceItemRepository;
import com.cue.demo.repositories.WatchedPerformanceItemRepository;
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

    /**
     * Adds a performance to the user's "watch later" list based on the provided data.
     *
     * @param dto Contains the user ID and performance ID to be associated with the "watch later" list entry.
     * @throws UserNotFoundByIdException If a user with the specified ID is not found.
     * @throws PerformanceNotFoundByIdException If a performance with the specified ID is not found.
     */
    @Transactional
    public void addItemToWatchLater(final WatchPerformanceItemDTO dto) throws UserNotFoundByIdException, PerformanceNotFoundByIdException {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new UserNotFoundByIdException(dto.userId()));
        Performance performance = performanceRepository.findById(dto.performanceId())
                .orElseThrow(() -> new PerformanceNotFoundByIdException(dto.performanceId()));

        if (watchLaterRepository.existsByUserIdAndPerformanceId(user.getId(), performance.getId())) {
            System.out.println("Performance  with id: " + performance.getId() + " already exists" +
                    "in user's " + user.getId() + " WATCH LATER LIST");
        } else {
            WatchLaterPerformanceItem item = WatchLaterPerformanceItem.builder()
                    .user(user)
                    .performance(performance)
                    .build();
            watchLaterRepository.save(item);
        }
    }

    /**
     * Removes a performance from the user's "watch later" list based on the provided data.
     *
     * @param dto Contains the user ID and performance ID related to the "watch later" list entry to be removed.
     * @throws UserNotFoundByIdException If a user with the specified ID is not found.
     * @throws PerformanceNotFoundByIdException If a performance with the specified ID is not found.
     * @throws PerformanceNotInUsersWatchListException If the specified performance is not found in the user's "watch later" list.
     */
    @Transactional
    public void deleteItemFromWatchLater(final WatchPerformanceItemDTO dto) throws PerformanceNotFoundByIdException, UserNotFoundByIdException {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new UserNotFoundByIdException(dto.userId()));
        Performance performance = performanceRepository.findById(dto.performanceId())
                .orElseThrow(() -> new PerformanceNotFoundByIdException(dto.performanceId()));

        if (watchLaterRepository.existsByUserIdAndPerformanceId(user.getId(), performance.getId())) {
            watchLaterRepository.deleteByUserIdAndPerformanceId(user.getId(), performance.getId());
        } else {
            throw new PerformanceNotInUsersWatchListException("Performance with id: " + performance.getId() +
                    "not found in user's" + user.getId() + " WATCH LATER LIST");
        }
    }

    /**
     * Gets a page of PerformanceCardDTOs from the user's "watch later" list.
     *
     * @param pageable The pageable object.
     * @param userId The ID of the user received in the HTTP header.
     * @return A page of performance card DTOs.
     * @throws UserNotFoundByIdException If a user with the specified ID is not found.
     */
    public Page<PerformanceCardDTO> getWatchLaterPerformanceCards(final Pageable pageable, final Long userId) throws UserNotFoundByIdException {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundByIdException(userId);
        }
        Page<WatchLaterPerformanceItem> watchLaterPage = watchLaterRepository.findByUserId(userId, pageable);
        return watchLaterPage.map(mapper::fromWatchLaterItemEntityToPerformanceCardDTO);
    }

    /**
     * Adds a performance to the user's "watched" list based on the provided data.
     *
     * @param dto Contains the user ID and performance ID to be associated with the "watched" list entry.
     * @throws UserNotFoundByIdException If a user with the specified ID is not found.
     * @throws PerformanceNotFoundByIdException If a performance with the specified ID is not found.
     */
    @Transactional
    public void addItemToWatched(final WatchPerformanceItemDTO dto) throws UserNotFoundByIdException, PerformanceNotFoundByIdException {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new UserNotFoundByIdException(dto.userId()));
        Performance performance = performanceRepository.findById(dto.performanceId())
                .orElseThrow(() -> new PerformanceNotFoundByIdException(dto.performanceId()));

        if (watchedPerformanceRepository.existsByUserIdAndPerformanceId(user.getId(), performance.getId())) {
            System.out.println("Performance  with id: " + performance.getId() + " already exists" +
                    "in user's " + user.getId() + " WATCHED LIST");
        } else {
            WatchedPerformanceItem item = WatchedPerformanceItem.builder()
                    .user(user)
                    .performance(performance)
                    .build();
            watchedPerformanceRepository.save(item);
        }
    }

    /**
     * Removes a performance from the user's "watched" list based on the provided data.
     *
     * @param dto Contains the user ID and performance ID related to the "watched" list entry to be removed.
     * @throws UserNotFoundByIdException If a user with the specified ID is not found.
     * @throws PerformanceNotFoundByIdException If a performance with the specified ID is not found.
     * @throws PerformanceNotInUsersWatchedListException If the specified performance is not found in the user's "watched" list.
     */
    @Transactional
    public void deleteItemFromWatched(final WatchPerformanceItemDTO dto) throws UserNotFoundByIdException, PerformanceNotFoundByIdException {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new UserNotFoundByIdException(dto.userId()));
        Performance performance = performanceRepository.findById(dto.performanceId())
                .orElseThrow(() -> new PerformanceNotFoundByIdException(dto.performanceId()));

        if (watchedPerformanceRepository.existsByUserIdAndPerformanceId(user.getId(), performance.getId())) {
            watchedPerformanceRepository.deleteByUserIdAndPerformanceId(user.getId(), performance.getId());

        } else {
            throw new PerformanceNotInUsersWatchedListException("Performance  with id: " + performance.getId() +
                    " does not exist in user's " + user.getId() + " WATCHED LIST");
        }
    }

    /**
     * Gets a page of PerformanceCardDTOs from the user's "watched" list.
     *
     * @param pageable The pageable object.
     * @param userId The ID of the user received in the HTTP header.
     * @return A page of performance card DTOs.
     * @throws UserNotFoundByIdException If a user with the specified ID is not found.
     */
    public Page<PerformanceCardDTO> getWatchedPerformanceCards(final Pageable pageable, final Long userId) throws UserNotFoundByIdException {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundByIdException(userId);
        }
        Page<WatchedPerformanceItem> watchedPage = watchedPerformanceRepository.findByUserId(userId, pageable);
        return watchedPage.map(mapper::fromWatchedItemEntityToPerformanceCardDTO);
    }

    /**
     * Returns true if performance with id is in the user's watch later list.
     * @param userId The ID of the user received in the HTTP header.
     * @param performanceId The ID of the performance that is checked.
     * @return True if the performance is in the list, false otherwise.
     */
    public Boolean isPerformanceInWatchLater(final Long userId, final Long performanceId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundByIdException(userId);
        }

        if (!performanceRepository.existsById(performanceId)) {
            throw new PerformanceNotFoundByIdException(performanceId);
        }

        return watchLaterRepository.existsByUserIdAndPerformanceId(userId, performanceId);
    }

    /**
     * Returns true if performance with id is in the user's watched list.
     * @param userId The ID of the user received in the HTTP header.
     * @param performanceId The ID of the performance that is checked.
     * @return True if the performance is in the list, false otherwise.
     */
    public Boolean isPerformanceInWatched(final Long userId, final Long performanceId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundByIdException(userId);
        }

        if (!performanceRepository.existsById(performanceId)) {
            throw new PerformanceNotFoundByIdException(performanceId);
        }

        return watchedPerformanceRepository.existsByUserIdAndPerformanceId(userId, performanceId);
    }
}
