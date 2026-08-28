package com.cue.demo.services;

import com.cue.demo.dtos.PerformanceCardDTO;
import com.cue.demo.dtos.WatchPerformanceItemDTO;
import com.cue.demo.entities.Performance;
import com.cue.demo.entities.User;
import com.cue.demo.entities.WatchLaterPerformanceItem;
import com.cue.demo.entities.WatchedPerformanceItem;
import com.cue.demo.exceptions.PerformanceNotFoundByIdException;
import com.cue.demo.mapper.PerformanceMapper;
import com.cue.demo.repositories.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public final class UserServiceTest {
    @Mock private PerformanceRepository performanceRepository;
    @Mock private  UserRepository userRepository;
    @Mock private WatchLaterPerformanceItemRepository watchLaterRepository;
    @Mock private WatchedPerformanceItemRepository watchedPerformanceRepository;
    @Mock private ReviewRepository reviewRepository;
    @Mock private PerformanceMapper mapper;

    @InjectMocks
    private UserService userService;

    final Long existingUserId = 10L;
    final Long existingPerformanceId = 5L;
    final Long nonExistingPerformanceId = 999L;

    /**
     * Tests the successful addition of a performance to a user's "Watch Later" list.
     */
    @Test
    void givenValidData_whenAddsToWatchLaterUserPerformance_thenAddToWatchLater ()
    {
        User testUser = User.builder().id(existingUserId).build();

        Performance testPerformance = Performance.builder()
                .id(existingPerformanceId).build();

        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(existingUserId, existingPerformanceId);

        Mockito.when(userRepository.getReferenceById(existingUserId)).thenReturn(testUser);
        Mockito.when(performanceRepository.findById(existingPerformanceId)).thenReturn(Optional.of(testPerformance));
        Mockito.when(watchLaterRepository.existsByUserIdAndPerformanceId(existingUserId, existingPerformanceId)).thenReturn(false);

        userService.addItemToWatchLater(dto);

        Mockito.verify(userRepository, Mockito.times(1)).getReferenceById(existingUserId);
        Mockito.verify(performanceRepository, Mockito.times(1)).findById(existingPerformanceId);
        Mockito.verify(watchLaterRepository, Mockito.times(1)).existsByUserIdAndPerformanceId(existingUserId, existingPerformanceId);
        Mockito.verify(watchLaterRepository, Mockito.times(1)).save(Mockito.any(WatchLaterPerformanceItem.class));
    }

    /**
     * Tests that a PerformanceNotFoundByIdException is thrown when a user attempts to add a non-existing performance to "Watch Later".
     */
    @Test
    void givenNonExistingPerformance_whenAddsToWatchLaterUserPerformance_thenThrowPerformanceNotFoundException ()
    {
        User testUser = User.builder().id(existingUserId).build();
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(existingUserId, nonExistingPerformanceId);

        Mockito.when(userRepository.getReferenceById(existingUserId)).thenReturn(testUser);
        Mockito.when(performanceRepository.findById(nonExistingPerformanceId)).thenReturn(Optional.empty());

        Assertions.assertThrows(PerformanceNotFoundByIdException.class, () -> userService.addItemToWatchLater(dto));

        Mockito.verify(userRepository, Mockito.times(1)).getReferenceById(existingUserId);
        Mockito.verify(performanceRepository, Mockito.times(1)).findById(nonExistingPerformanceId);
        Mockito.verify(watchLaterRepository, Mockito.never()).existsByUserIdAndPerformanceId(Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(watchLaterRepository, Mockito.never()).save(Mockito.any());
    }

    /**
     * Tests the successful deletion of a performance from a user's "Watch Later" list.
     */
    @Test
    void givenValidData_whenDeletesItemFromWatchLater_thenDeleteItemFromWatchLater ()
    {
        Performance testPerformance = Performance.builder()
                .id(existingPerformanceId).build();

        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(existingUserId, existingPerformanceId);

        Mockito.when(performanceRepository.findById(existingPerformanceId)).thenReturn(Optional.of(testPerformance));
        Mockito.when(watchLaterRepository.existsByUserIdAndPerformanceId(existingUserId, existingPerformanceId)).thenReturn(true);

        userService.deleteItemFromWatchLater(dto);
        Mockito.verify(performanceRepository, Mockito.times(1)).findById(existingPerformanceId);
        Mockito.verify(watchLaterRepository, Mockito.times(1)).existsByUserIdAndPerformanceId(existingUserId, existingPerformanceId);
        Mockito.verify(watchLaterRepository,
                Mockito.times(1)).deleteByUserIdAndPerformanceId(existingUserId, existingPerformanceId);
    }


    /**
     * Tests that a PerformanceNotFoundByIdException is thrown when a user attempts to delete a non-existing performance from "Watch Later".
     */
    @Test
    void givenNonExistingPerformance_whenDeletesItemFromWatchLater_thenThrowPerformanceNotFoundException ()
    {
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(existingUserId, nonExistingPerformanceId);
        Mockito.when(performanceRepository.findById(nonExistingPerformanceId)).thenReturn(Optional.empty());

        Assertions.assertThrows(PerformanceNotFoundByIdException.class, () -> userService.deleteItemFromWatchLater(dto));

        Mockito.verify(performanceRepository, Mockito.times(1)).findById(nonExistingPerformanceId);
        Mockito.verify(watchLaterRepository, Mockito.never()).existsByUserIdAndPerformanceId(Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(watchLaterRepository, Mockito.never()).deleteByUserIdAndPerformanceId(Mockito.anyLong(), Mockito.anyLong());
    }

    /**
     * Tests the successful retrieval of a user's "Watch Later" performances as card DTOs.
     */
    @Test
    void givenValidData_whenGetWatchLaterPerformanceCards_thenGetWatchLaterPerformanceCards ()
    {
       final Long existingWatchLaterItemId = 150L;
       Pageable pageable = PageRequest.of(0, 10);

       WatchLaterPerformanceItem watchLaterMockItem = WatchLaterPerformanceItem.builder().id(existingWatchLaterItemId).build();
       Page<WatchLaterPerformanceItem> watchLaterMockPage = new PageImpl<>(List.of(watchLaterMockItem));

        PerformanceCardDTO expectedPerformanceCardDto = PerformanceCardDTO.builder().id(existingPerformanceId).build();

       Mockito.when(watchLaterRepository.findByUserId(existingUserId, pageable)).thenReturn(watchLaterMockPage);
       Mockito.when(mapper.fromWatchLaterItemEntityToPerformanceCardDTO(watchLaterMockItem)).thenReturn(expectedPerformanceCardDto);

       Page<PerformanceCardDTO> result = userService.getWatchLaterPerformanceCards(existingUserId, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals(expectedPerformanceCardDto, result.getContent().getFirst());

        Mockito.verify(watchLaterRepository, Mockito.times(1)).findByUserId(existingUserId, pageable);
        Mockito.verify(mapper, Mockito.times(1)).fromWatchLaterItemEntityToPerformanceCardDTO(watchLaterMockItem);
    }

    /**
     * Tests the successful addition of a performance to a user's "Watched" list.
     */
    @Test
    void givenValidData_whenAddsToWatchedUserPerformance_thenAddToWatched ()
    {
        User testUser = User.builder().id(existingUserId).build();

        Performance testPerformance = Performance.builder()
                .id(existingPerformanceId).build();

        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(existingUserId, existingPerformanceId);

        Mockito.when(userRepository.getReferenceById(existingUserId)).thenReturn(testUser);
        Mockito.when(performanceRepository.findById(existingPerformanceId)).thenReturn(Optional.of(testPerformance));
        Mockito.when(watchedPerformanceRepository.existsByUserIdAndPerformanceId(existingUserId, existingPerformanceId)).thenReturn(false);

        userService.addItemToWatched(dto);

        Mockito.verify(userRepository, Mockito.times(1)).getReferenceById(existingUserId);
        Mockito.verify(performanceRepository, Mockito.times(1)).findById(existingPerformanceId);
        Mockito.verify(watchedPerformanceRepository, Mockito.times(1)).existsByUserIdAndPerformanceId(existingUserId, existingPerformanceId);
        Mockito.verify(watchedPerformanceRepository, Mockito.times(1)).save(Mockito.any(WatchedPerformanceItem.class));
    }

    /**
     * Tests that a PerformanceNotFoundByIdException is thrown when a user attempts to add a non-existing performance to "Watched".
     */
    @Test
    void givenNonExistingPerformance_whenAddsToWatchedUserPerformance_thenThrowPerformanceNotFoundException ()
    {
        User testUser = User.builder().id(existingUserId).build();
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(existingUserId, nonExistingPerformanceId);

        Mockito.when(userRepository.getReferenceById(existingUserId)).thenReturn(testUser);
        Mockito.when(performanceRepository.findById(nonExistingPerformanceId)).thenReturn(Optional.empty());

        Assertions.assertThrows(PerformanceNotFoundByIdException.class, () -> userService.addItemToWatched(dto));

        Mockito.verify(userRepository, Mockito.times(1)).getReferenceById(existingUserId);
        Mockito.verify(performanceRepository, Mockito.times(1)).findById(nonExistingPerformanceId);
        Mockito.verify(watchedPerformanceRepository, Mockito.never()).existsByUserIdAndPerformanceId(Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(watchedPerformanceRepository, Mockito.never()).save(Mockito.any());
    }

    /**
     * Tests the successful deletion of a performance from a user's "Watched" list.
     */
    @Test
    void givenValidData_whenDeletesItemFromWatched_thenDeleteItemFromWatched ()
    {
        Performance testPerformance = Performance.builder()
                .id(existingPerformanceId).build();

        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(existingUserId, existingPerformanceId);

        Mockito.when(performanceRepository.findById(existingPerformanceId)).thenReturn(Optional.of(testPerformance));
        Mockito.when(watchedPerformanceRepository.existsByUserIdAndPerformanceId(existingUserId, existingPerformanceId)).thenReturn(true);

        userService.deleteItemFromWatched(dto);
        Mockito.verify(performanceRepository, Mockito.times(1)).findById(existingPerformanceId);
        Mockito.verify(watchedPerformanceRepository, Mockito.times(1)).existsByUserIdAndPerformanceId(existingUserId, existingPerformanceId);
        Mockito.verify(watchedPerformanceRepository,
                Mockito.times(1)).deleteByUserIdAndPerformanceId(existingUserId, existingPerformanceId);
    }

    /**
     * Tests that a PerformanceNotFoundByIdException is thrown when a user attempts to delete a non-existing performance from "Watched".
     */
    @Test
    void givenNonExistingPerformance_whenDeletesItemFromWatched_thenThrowPerformanceNotFoundException ()
    {
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(existingUserId, nonExistingPerformanceId);
        Mockito.when(performanceRepository.findById(nonExistingPerformanceId)).thenReturn(Optional.empty());

        Assertions.assertThrows(PerformanceNotFoundByIdException.class, () -> userService.deleteItemFromWatched(dto));

        Mockito.verify(performanceRepository, Mockito.times(1)).findById(nonExistingPerformanceId);
        Mockito.verify(watchedPerformanceRepository, Mockito.never()).existsByUserIdAndPerformanceId(Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(watchedPerformanceRepository, Mockito.never()).deleteByUserIdAndPerformanceId(Mockito.anyLong(), Mockito.anyLong());
    }

    /**
     * Tests the successful retrieval of a user's "Watched" performances as card DTOs.
     */
    @Test
    void givenValidData_whenGetWatchedPerformanceCards_thenGetWatchedPerformanceCards ()
    {
        final Long existingWatchedItemId = 150L;
        Pageable pageable = PageRequest.of(0, 10);

        WatchedPerformanceItem watchedMockItem = WatchedPerformanceItem.builder().id(existingWatchedItemId).build();
        Page<WatchedPerformanceItem> watchedMockPage = new PageImpl<>(List.of(watchedMockItem));

        PerformanceCardDTO expectedPerformanceCardDto = PerformanceCardDTO.builder().id(existingPerformanceId).build();

        Mockito.when(watchedPerformanceRepository.findByUserId(existingUserId, pageable)).thenReturn(watchedMockPage);
        Mockito.when(mapper.fromWatchedItemEntityToPerformanceCardDTO(watchedMockItem)).thenReturn(expectedPerformanceCardDto);

        Page<PerformanceCardDTO> result = userService.getWatchedPerformanceCards(existingUserId, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals(expectedPerformanceCardDto, result.getContent().getFirst());

        Mockito.verify(watchedPerformanceRepository, Mockito.times(1)).findByUserId(existingUserId, pageable);
        Mockito.verify(mapper, Mockito.times(1)).fromWatchedItemEntityToPerformanceCardDTO(watchedMockItem);
    }

    /**
     * Tests the successful retrieval of performances reviewed by a user as card DTOs.
     */
    @Test
    void givenValidData_whenGetReviewedPerformanceCards_thenGetReviewedPerformanceCards ()
    {
        Pageable pageable = PageRequest.of(0, 10);

        Performance reviewedPerformanceMock = Performance.builder().id(existingPerformanceId).build();

        Page<Performance> reviewedPageMock = new PageImpl<>(List.of(reviewedPerformanceMock));

        PerformanceCardDTO expectedPerformanceCardDto = PerformanceCardDTO.builder().id(existingPerformanceId).build();

        Mockito.when(reviewRepository.findPerformancesReviewedByUserId(existingUserId, pageable)).thenReturn(reviewedPageMock);
        Mockito.when(mapper.fromPerformanceEntityToPerformanceCardDTO(reviewedPerformanceMock)).thenReturn(expectedPerformanceCardDto);

        Page<PerformanceCardDTO> result = userService.getReviewedPerformanceCards(existingUserId, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals(expectedPerformanceCardDto, result.getContent().getFirst());

        Mockito.verify(reviewRepository, Mockito.times(1)).findPerformancesReviewedByUserId(existingUserId, pageable);
        Mockito.verify(mapper, Mockito.times(1)).fromPerformanceEntityToPerformanceCardDTO(reviewedPerformanceMock);
    }


}
