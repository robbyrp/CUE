package com.cue.demo.services;

import com.cue.demo.dtos.WatchPerformanceItemDTO;
import com.cue.demo.entities.Performance;
import com.cue.demo.entities.User;
import com.cue.demo.entities.WatchLaterPerformanceItem;
import com.cue.demo.entities.WatchedPerformanceItem;
import com.cue.demo.exceptions.ItemAlreadyInWatchListException;
import com.cue.demo.exceptions.PerformanceNotFoundByIdException;
import com.cue.demo.exceptions.PerformanceNotInUsersWatchListException;
import com.cue.demo.exceptions.UserNotFoundByIdException;
import com.cue.demo.repositories.PerformanceRepository;
import com.cue.demo.repositories.UserRepository;
import com.cue.demo.repositories.WatchLaterPerformanceItemRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class MobileUserService {
    private final PerformanceRepository performanceRepository;
    private final UserRepository userRepository;
    private final WatchLaterPerformanceItemRepository watchLaterRepository;

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
                .orElseThrow(() -> new UserNotFoundByIdException("User with id: " + dto.userId() + " not found"));
        Performance performance = performanceRepository.findById(dto.performanceId())
                .orElseThrow(() -> new PerformanceNotFoundByIdException("Performance with id: " + dto.performanceId() + " not found"));

        if (watchLaterRepository.existsByUserIdAndPerformanceId(dto.userId(), dto.performanceId())) {
            throw new ItemAlreadyInWatchListException("Performance  with id: " + dto.performanceId() + " already exists" +
                    "in user's " + dto.userId() + " WATCH LATER LIST");
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
    public void deleteItemFromWatchLater(final WatchPerformanceItemDTO dto) throws UserNotFoundByIdException, PerformanceNotFoundByIdException, UserNotFoundByIdException {
        Performance performance = performanceRepository.findById(dto.performanceId())
                .orElseThrow(() -> new PerformanceNotFoundByIdException("Performance with id: " + dto.performanceId() + " not found"));
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new UserNotFoundByIdException("User with id: " + dto.userId() + " not found"));
        if (watchLaterRepository.existsByUserIdAndPerformanceId(dto.userId(), dto.performanceId())) {
            watchLaterRepository.deleteByUserIdAndPerformanceId(dto.userId(), dto.performanceId());
        } else {
            throw new PerformanceNotInUsersWatchListException("Performance with id: " + dto.performanceId() +
                    "not found in user's" + user.getId() + " WATCH LATER LIST");
        }
    }

}
