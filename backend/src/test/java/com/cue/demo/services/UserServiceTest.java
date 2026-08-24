package com.cue.demo.services;

import com.cue.demo.dtos.PerformanceCardDTO;
import com.cue.demo.dtos.WatchPerformanceItemDTO;
import com.cue.demo.entities.Performance;
import com.cue.demo.entities.User;
import com.cue.demo.entities.WatchLaterPerformanceItem;
import com.cue.demo.entities.WatchedPerformanceItem;
import com.cue.demo.exceptions.PerformanceNotFoundByIdException;
import com.cue.demo.exceptions.UserNotFoundByIdException;
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

    //-------------WATCH LATER------------------
    @Test
    void givenValidData_whenAddsToWatchLaterUserPerformance_thenAddToWatchLater () {
        final Long existingUserId = 10L;
        final Long existingPerformanceId = 5L;
        User testUser = User.builder().id(existingUserId).build();

        Performance testPerformance = Performance.builder()
                .id(existingPerformanceId).build();

        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(existingUserId, existingPerformanceId);

        Mockito.when(userRepository.findById(existingUserId)).thenReturn(Optional.of(testUser));
        Mockito.when(performanceRepository.findById(existingPerformanceId)).thenReturn(Optional.of(testPerformance));
        Mockito.when(watchLaterRepository.existsByUserIdAndPerformanceId(existingUserId, existingPerformanceId)).thenReturn(false);

        userService.addItemToWatchLater(dto);

        Mockito.verify(watchLaterRepository, Mockito.times(1)).save(Mockito.any(WatchLaterPerformanceItem.class));
    }

    @Test
    void givenNonExistingUser_whenAddsToWatchLaterUserPerformance_thenThrowUserNotFoundException () {
        final Long nonExistingUserId = 999L;
        final Long existingPerformanceId = 5L;
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(nonExistingUserId, existingPerformanceId);
        Mockito.when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundByIdException.class, () -> userService.addItemToWatchLater(dto));

        Mockito.verify(watchLaterRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void givenNonExistingPerformance_whenAddsToWatchLaterUserPerformance_thenThrowPerformanceNotFoundException () {
        final Long existingUserId = 10L;
        final Long nonExistingPerformanceId = 999L;

        User testUser = User.builder().id(existingUserId).build();
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(existingUserId, nonExistingPerformanceId);

        Mockito.when(userRepository.findById(existingUserId)).thenReturn(Optional.of(testUser));
        Mockito.when(performanceRepository.findById(nonExistingPerformanceId)).thenReturn(Optional.empty());

        Assertions.assertThrows(PerformanceNotFoundByIdException.class, () -> userService.addItemToWatchLater(dto));

        Mockito.verify(watchLaterRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void givenValidData_whenDeletesItemFromWatchLater_thenDeleteItemFromWatchLater () {
        final Long existingUserId = 10L;
        final Long existingPerformanceId = 5L;

        User testUser = User.builder().id(existingUserId).build();
        Performance testPerformance = Performance.builder()
                .id(existingPerformanceId).build();

        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(existingUserId, existingPerformanceId);

        Mockito.when(userRepository.findById(existingUserId)).thenReturn(Optional.of(testUser));
        Mockito.when(performanceRepository.findById(existingPerformanceId)).thenReturn(Optional.of(testPerformance));
        Mockito.when(watchLaterRepository.existsByUserIdAndPerformanceId(existingUserId, existingPerformanceId)).thenReturn(true);

        userService.deleteItemFromWatchLater(dto);
        Mockito.verify(watchLaterRepository,
                Mockito.times(1)).deleteByUserIdAndPerformanceId(existingUserId, existingPerformanceId);
    }

    @Test
    void givenNonExistingUser_whenDeletesItemFromWatchLater_thenThrowUserNotFoundException () {
        final Long nonExistingUserId = 999L;
        final Long existingPerformanceId = 5L;

        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(nonExistingUserId, existingPerformanceId);
        Mockito.when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundByIdException.class, () -> userService.deleteItemFromWatchLater(dto));

        Mockito.verify(watchLaterRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void givenNonExistingPerformance_whenDeletesItemFromWatchLater_thenThrowPerformanceNotFoundException () {
        final Long existingUserId = 10L;
        final Long nonExistingPerformanceId = 999L;

        User testUser = User.builder().id(existingUserId).build();
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(existingUserId, nonExistingPerformanceId);
        Mockito.when(userRepository.findById(existingUserId)).thenReturn(Optional.of(testUser));
        Mockito.when(performanceRepository.findById(nonExistingPerformanceId)).thenReturn(Optional.empty());

        Assertions.assertThrows(PerformanceNotFoundByIdException.class, () -> userService.deleteItemFromWatchLater(dto));

        Mockito.verify(watchLaterRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void givenValidData_whenGetWatchLaterPerformanceCards_thenGetWatchLaterPerformanceCards () {
       final Long existingUserId = 10L;
       final Long existingPerformanceId = 5L;
       final Long existingWatchLaterItemId = 150L;
       Pageable pageable = PageRequest.of(0, 10);

       WatchLaterPerformanceItem watchLaterMockItem = WatchLaterPerformanceItem.builder().id(existingWatchLaterItemId).build();
       Page<WatchLaterPerformanceItem> watchLaterMockPage = new PageImpl<>(List.of(watchLaterMockItem));

        PerformanceCardDTO expectedPerformanceCardDto = PerformanceCardDTO.builder().id(existingPerformanceId).build();

       Mockito.when(userRepository.existsById(existingUserId)).thenReturn(true);
       Mockito.when(watchLaterRepository.findByUserId(existingUserId, pageable)).thenReturn(watchLaterMockPage);
       Mockito.when(mapper.fromWatchLaterItemEntityToPerformanceCardDTO(watchLaterMockItem)).thenReturn(expectedPerformanceCardDto);

       Page<PerformanceCardDTO> result = userService.getWatchLaterPerformanceCards(existingUserId, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals(expectedPerformanceCardDto, result.getContent().getFirst());
    }

    @Test
    void givenNonExistingUser_whenGetWatchLaterPerformanceCards_thenThrowUserNotFoundException () {
        final Long nonExistingUserId = 999L;
        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(userRepository.existsById(nonExistingUserId)).thenReturn(false);

        Assertions.assertThrows(UserNotFoundByIdException.class, () -> userService.getWatchLaterPerformanceCards(nonExistingUserId, pageable));
    }

    //-------------WATCHED------------------
    @Test
    void givenValidData_whenAddsToWatchedUserPerformance_thenAddToWatched () {
        final Long existingUserId = 10L;
        final Long existingPerformanceId = 5L;
        User testUser = User.builder().id(existingUserId).build();

        Performance testPerformance = Performance.builder()
                .id(existingPerformanceId).build();

        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(existingUserId, existingPerformanceId);

        Mockito.when(userRepository.findById(existingUserId)).thenReturn(Optional.of(testUser));
        Mockito.when(performanceRepository.findById(existingPerformanceId)).thenReturn(Optional.of(testPerformance));
        Mockito.when(watchedPerformanceRepository.existsByUserIdAndPerformanceId(existingUserId, existingPerformanceId)).thenReturn(false);

        userService.addItemToWatched(dto);

        Mockito.verify(watchedPerformanceRepository, Mockito.times(1)).save(Mockito.any(WatchedPerformanceItem.class));
    }

    @Test
    void givenNonExistingUser_whenAddsToWatchedUserPerformance_thenThrowUserNotFoundException () {
        final Long nonExistingUserId = 999L;
        final Long existingPerformanceId = 5L;
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(nonExistingUserId, existingPerformanceId);
        Mockito.when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundByIdException.class, () -> userService.addItemToWatched(dto));

        Mockito.verify(watchedPerformanceRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void givenNonExistingPerformance_whenAddsToWatchedUserPerformance_thenThrowPerformanceNotFoundException () {
        final Long existingUserId = 10L;
        final Long nonExistingPerformanceId = 999L;

        User testUser = User.builder().id(existingUserId).build();
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(existingUserId, nonExistingPerformanceId);

        Mockito.when(userRepository.findById(existingUserId)).thenReturn(Optional.of(testUser));
        Mockito.when(performanceRepository.findById(nonExistingPerformanceId)).thenReturn(Optional.empty());

        Assertions.assertThrows(PerformanceNotFoundByIdException.class, () -> userService.addItemToWatched(dto));

        Mockito.verify(watchedPerformanceRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void givenValidData_whenDeletesItemFromWatched_thenDeleteItemFromWatched () {
        final Long existingUserId = 10L;
        final Long existingPerformanceId = 5L;

        User testUser = User.builder().id(existingUserId).build();
        Performance testPerformance = Performance.builder()
                .id(existingPerformanceId).build();

        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(existingUserId, existingPerformanceId);

        Mockito.when(userRepository.findById(existingUserId)).thenReturn(Optional.of(testUser));
        Mockito.when(performanceRepository.findById(existingPerformanceId)).thenReturn(Optional.of(testPerformance));
        Mockito.when(watchedPerformanceRepository.existsByUserIdAndPerformanceId(existingUserId, existingPerformanceId)).thenReturn(true);

        userService.deleteItemFromWatched(dto);
        Mockito.verify(watchedPerformanceRepository,
                Mockito.times(1)).deleteByUserIdAndPerformanceId(existingUserId, existingPerformanceId);
    }

    @Test
    void givenNonExistingUser_whenDeletesItemFromWatched_thenThrowUserNotFoundException () {
        final Long nonExistingUserId = 999L;
        final Long existingPerformanceId = 5L;

        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(nonExistingUserId, existingPerformanceId);
        Mockito.when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundByIdException.class, () -> userService.deleteItemFromWatched(dto));

        Mockito.verify(watchedPerformanceRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void givenNonExistingPerformance_whenDeletesItemFromWatched_thenThrowPerformanceNotFoundException () {
        final Long existingUserId = 10L;
        final Long nonExistingPerformanceId = 999L;

        User testUser = User.builder().id(existingUserId).build();
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(existingUserId, nonExistingPerformanceId);
        Mockito.when(userRepository.findById(existingUserId)).thenReturn(Optional.of(testUser));
        Mockito.when(performanceRepository.findById(nonExistingPerformanceId)).thenReturn(Optional.empty());

        Assertions.assertThrows(PerformanceNotFoundByIdException.class, () -> userService.deleteItemFromWatched(dto));

        Mockito.verify(watchedPerformanceRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void givenValidData_whenGetWatchedPerformanceCards_thenGetWatchedPerformanceCards () {
        final Long existingUserId = 10L;
        final Long existingPerformanceId = 5L;
        final Long existingWatchedItemId = 150L;
        Pageable pageable = PageRequest.of(0, 10);

        WatchedPerformanceItem watchedMockItem = WatchedPerformanceItem.builder().id(existingWatchedItemId).build();
        Page<WatchedPerformanceItem> watchedMockPage = new PageImpl<>(List.of(watchedMockItem));

        PerformanceCardDTO expectedPerformanceCardDto = PerformanceCardDTO.builder().id(existingPerformanceId).build();

        Mockito.when(userRepository.existsById(existingUserId)).thenReturn(true);
        Mockito.when(watchedPerformanceRepository.findByUserId(existingUserId, pageable)).thenReturn(watchedMockPage);
        Mockito.when(mapper.fromWatchedItemEntityToPerformanceCardDTO(watchedMockItem)).thenReturn(expectedPerformanceCardDto);

        Page<PerformanceCardDTO> result = userService.getWatchedPerformanceCards(existingUserId, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals(expectedPerformanceCardDto, result.getContent().getFirst());
    }

    @Test
    void givenNonExistingUser_whenGetWatchedPerformanceCards_thenThrowUserNotFoundException () {
        final Long nonExistingUserId = 999L;
        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(userRepository.existsById(nonExistingUserId)).thenReturn(false);

        Assertions.assertThrows(UserNotFoundByIdException.class, () -> userService.getWatchedPerformanceCards(nonExistingUserId, pageable));
    }

    //-------------REVIEWED------------------
    @Test
    void givenValidData_whenGetReviewedPerformanceCards_thenGetReviewedPerformanceCards () {
        final Long existingUserId = 10L;
        final Long existingPerformanceId = 5L;
        Pageable pageable = PageRequest.of(0, 10);

        Performance reviewedPerformanceMock = Performance.builder().id(existingPerformanceId).build();

        Page<Performance> reviewedPageMock = new PageImpl<>(List.of(reviewedPerformanceMock));

        PerformanceCardDTO expectedPerformanceCardDto = PerformanceCardDTO.builder().id(existingPerformanceId).build();

        Mockito.when(userRepository.existsById(existingUserId)).thenReturn(true);
        Mockito.when(reviewRepository.findPerformancesReviewedByUserId(existingUserId, pageable)).thenReturn(reviewedPageMock);
        Mockito.when(mapper.fromPerformanceEntityToPerformanceCardDTO(reviewedPerformanceMock)).thenReturn(expectedPerformanceCardDto);

        Page<PerformanceCardDTO> result = userService.getReviewedPerformanceCards(existingUserId, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals(expectedPerformanceCardDto, result.getContent().getFirst());
    }

    @Test
    void givenNonExistingUser_whenGetReviewedPerformanceCards_thenThrowUserNotFoundException () {
        final Long nonExistingUserId = 10L;
        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(userRepository.existsById(nonExistingUserId)).thenReturn(false);

        Assertions.assertThrows(UserNotFoundByIdException.class, () -> userService.getReviewedPerformanceCards(nonExistingUserId, pageable));
    }


}
