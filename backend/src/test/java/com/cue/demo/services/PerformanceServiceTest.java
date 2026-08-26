package com.cue.demo.services;

import com.cue.demo.dtos.PerformanceCardDTO;
import com.cue.demo.dtos.PerformanceDTO;
import com.cue.demo.dtos.SearchSuggestion;
import com.cue.demo.entities.Performance;
import com.cue.demo.entities.User;
import com.cue.demo.enums.UserRole;
import com.cue.demo.exceptions.PerformanceAlreadyExistsException;
import com.cue.demo.exceptions.PerformanceIdMismatchException;
import com.cue.demo.exceptions.PerformanceNotFoundByIdException;
import com.cue.demo.exceptions.UserNotAuthorizedException;
import com.cue.demo.mapper.PerformanceMapper;
import com.cue.demo.repositories.PerformanceRepository;
import com.cue.demo.repositories.UserRepository;
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
public class PerformanceServiceTest {
    @Mock private PerformanceRepository performanceRepository;
    @Mock private UserRepository userRepository;
    @Mock private PerformanceMapper performanceMapper;

    @InjectMocks private PerformanceService service;

    final Long existingUserId = 10L;
    final Long existingPerformanceId = 10L;
    final Long invalidPerformanceId = 999L;

    @Test
    void givenValidData_whenCreatePerformance_thenReturnPerformance()
    {
        final String title = "Test title";
        final String director = "Test director";

        User testUser = User.builder().id(existingUserId).role(UserRole.ADMIN).build();

        PerformanceDTO testPerformanceDTO = PerformanceDTO.builder()
                .title(title)
                .director(director)
                .build();

        Mockito.when(userRepository.findById(existingUserId)).thenReturn(Optional.of(testUser));
        Mockito.when(performanceRepository.existsByTitleAndDirector(title, director)).thenReturn(false);

        service.createPerformance(existingUserId, testPerformanceDTO);

        Mockito.verify(userRepository, Mockito.times(1)).findById(existingUserId);
        Mockito.verify(performanceRepository, Mockito.times(1)).existsByTitleAndDirector(title, director);
        Mockito.verify(performanceRepository, Mockito.times(1)).save(Mockito.any(Performance.class));
    }

    @Test
    void givenDuplicatePerformance_whenCreatePerformance_thenThrowPerformanceAlreadyExistsException()
    {
        final String title = "Test title";
        final String director = "Test director";

        User testUser = User.builder().id(existingUserId).role(UserRole.ADMIN).build();

        PerformanceDTO testPerformanceDTO = PerformanceDTO.builder()
                .title(title)
                .director(director)
                .build();

        Mockito.when(userRepository.findById(existingUserId)).thenReturn(Optional.of(testUser));
        Mockito.when(performanceRepository.existsByTitleAndDirector(title, director)).thenReturn(true);

        Assertions.assertThrows(PerformanceAlreadyExistsException.class, () -> service.createPerformance(existingUserId, testPerformanceDTO));

        Mockito.verify(userRepository, Mockito.times(1)).findById(existingUserId);
        Mockito.verify(performanceRepository, Mockito.times(1)).existsByTitleAndDirector(title, director);
        Mockito.verify(performanceRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void givenInvalidUserRole_whenCreatePerformance_thenThrowUserNotAuthorizedException()
    {
        final String title = "Test title";
        final String director = "Test director";

        User testUser = User.builder().id(existingUserId).role(UserRole.USER).build();

        PerformanceDTO testPerformanceDTO = PerformanceDTO.builder()
                .title(title)
                .director(director)
                .build();

        Mockito.when(userRepository.findById(existingUserId)).thenReturn(Optional.of(testUser));

        Assertions.assertThrows(UserNotAuthorizedException.class, () -> service.createPerformance(existingUserId, testPerformanceDTO));

        Mockito.verify(userRepository, Mockito.times(1)).findById(existingUserId);
        Mockito.verify(performanceRepository, Mockito.never()).existsByTitleAndDirector(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(performanceRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void givenValidPerformanceId_whenGetPerformanceById_thenReturnPerformance()
    {
        PerformanceDTO dto = PerformanceDTO.builder().id(existingPerformanceId).build();
        Performance performanceMock = Performance.builder().id(existingPerformanceId).build();

        Mockito.when(performanceRepository.findById(existingPerformanceId)).thenReturn(Optional.of(performanceMock));
        Mockito.when(performanceMapper.fromPerformanceEntityToPerformanceDTO(performanceMock)).thenReturn(dto);

        service.getPerformanceById(existingPerformanceId);

        Mockito.verify(performanceRepository, Mockito.times(1)).findById(existingPerformanceId);
        Mockito.verify(performanceMapper, Mockito.times(1)).fromPerformanceEntityToPerformanceDTO(performanceMock);
    }

    @Test
    void givenInvalidPerformanceId_whenGetPerformanceById_thenThrowPerformanceNotFoundByIdException()
    {
        Performance performanceMock = Performance.builder().id(existingPerformanceId).build();

        Mockito.when(performanceRepository.findById(existingPerformanceId)).thenReturn(Optional.empty());

        Assertions.assertThrows(PerformanceNotFoundByIdException.class, () -> service.getPerformanceById(existingPerformanceId));

        Mockito.verify(performanceRepository, Mockito.times(1)).findById(existingPerformanceId);
        Mockito.verify(performanceMapper, Mockito.never()).fromPerformanceEntityToPerformanceDTO(performanceMock);
    }

    /**
     * This test simulates a page of 1000 Performance elements.
     */
    @Test
    void whenGetPerformances_thenReturnPerformances()
    {
        final int pageNumber = 0;
        final int pageSize = 2;
        final Long p1Id = 1L;
        final Long p2Id = 2L;
        final Long totalElements = 1000L;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Performance p1 = Performance.builder().id(p1Id).build();
        Performance p2 = Performance.builder().id(p2Id).build();
        List<Performance> performancesMocks = List.of(p1, p2);

        Page<Performance> performanceMocksPage = new PageImpl<>(performancesMocks, pageable, totalElements);
        PerformanceDTO expectedPerformanceDTO = PerformanceDTO.builder().id(p1Id).build();

        Mockito.when(performanceRepository.findAll(pageable)).thenReturn(performanceMocksPage);
        Mockito.when(performanceMapper.fromPerformanceEntityToPerformanceDTO(Mockito.any(Performance.class)))
                .thenReturn(expectedPerformanceDTO);

        Page<PerformanceDTO> result = service.getPerformances(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(pageSize,  result.getContent().size());
        Assertions.assertEquals(totalElements, result.getTotalElements());
        Assertions.assertEquals(totalElements / pageSize, result.getTotalPages());
        Assertions.assertEquals(expectedPerformanceDTO, result.getContent().getFirst());

        Mockito.verify(performanceRepository, Mockito.times(1)).findAll(pageable);
        Mockito.verify(performanceMapper, Mockito.times(pageSize)).fromPerformanceEntityToPerformanceDTO(Mockito.any(Performance.class));
    }

    @Test
    void givenValidData_whenDeletePerformance_thenDeletePerformance()
    {
        User testUser = User.builder().id(existingUserId).role(UserRole.ADMIN).build();

        Mockito.when(userRepository.findById(existingUserId)).thenReturn(Optional.of(testUser));
        Mockito.when(performanceRepository.existsById(existingPerformanceId)).thenReturn(true);

        service.deletePerformanceById(existingUserId, existingPerformanceId);

        Mockito.verify(userRepository, Mockito.times(1)).findById(existingUserId);
        Mockito.verify(performanceRepository, Mockito.times(1)).existsById(existingPerformanceId);
        Mockito.verify(performanceRepository, Mockito.times(1)).deleteById(existingPerformanceId);
    }

    @Test
    void givenInvalidPerformance_whenDeletePerformance_thenThrowPerformanceNotFoundByIdException()
    {
        User testUser = User.builder().id(existingUserId).role(UserRole.ADMIN).build();

        Mockito.when(userRepository.findById(existingUserId)).thenReturn(Optional.of(testUser));
        Mockito.when(performanceRepository.existsById(invalidPerformanceId)).thenReturn(false);

        Assertions.assertThrows(PerformanceNotFoundByIdException.class, () ->
                service.deletePerformanceById(existingUserId, invalidPerformanceId));

        Mockito.verify(userRepository, Mockito.times(1)).findById(existingUserId);
        Mockito.verify(performanceRepository, Mockito.times(1)).existsById(invalidPerformanceId);
        Mockito.verify(performanceRepository, Mockito.never()).deleteById(Mockito.anyLong());
        Mockito.verify(performanceRepository, Mockito.never()).delete(Mockito.any(Performance.class));
    }

    @Test
    void givenInvalidUserRole_whenDeletePerformance_thenThrowUserNotAuthorizedException()
    {
        User testUser = User.builder().id(existingUserId).role(UserRole.USER).build();

        Mockito.when(userRepository.findById(existingUserId)).thenReturn(Optional.of(testUser));

        Assertions.assertThrows(UserNotAuthorizedException.class, () ->
                service.deletePerformanceById(existingUserId, existingPerformanceId));

        Mockito.verify(userRepository, Mockito.times(1)).findById(existingUserId);
        Mockito.verify(performanceRepository, Mockito.never()).existsById(Mockito.anyLong());
        Mockito.verify(performanceRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }

    /**
     * This test merges the transferable fields from the updated performance DTO to the
     * already existing performance entity,
     * maintaining its existing fields: id, createdAt, viewsCount, averageRating, and deleted fields, as they
     * are not modifiable through the mapper.update() method.
     */
    @Test
    void giveValidData_whenUpdatePerformanceById_thenUpdatePerformanceById ()
    {
        final Long pathVariablePerformanceId = 10L;
        final String oldTitle = "Old title";
        final String updatedTitle = "Updated Title";

        User testUser = User.builder().id(existingUserId).role(UserRole.ADMIN).build();
        PerformanceDTO requestBodyPerformanceDto = PerformanceDTO.builder().id(pathVariablePerformanceId).title(updatedTitle).build();
        Performance performance =  Performance.builder().id(existingPerformanceId).title(oldTitle).build();

        PerformanceDTO mergedPerformanceDto = PerformanceDTO.builder().id(existingPerformanceId).title(updatedTitle).build();

        Mockito.when(userRepository.findById(existingUserId)).thenReturn(Optional.of(testUser));
        Mockito.when(performanceRepository.findById(pathVariablePerformanceId)).thenReturn(Optional.of(performance));
        Mockito.doAnswer(invocation -> {
            Performance p = invocation.getArgument(1);
            p.setTitle(updatedTitle);
            return null;
        }).when(performanceMapper).updatePerformanceEntityFromPerformanceDTO(requestBodyPerformanceDto, performance);

        Mockito.when(performanceRepository.save(performance)).thenReturn(performance);
        Mockito.when(performanceMapper.fromPerformanceEntityToPerformanceDTO(performance)).thenReturn(mergedPerformanceDto);

        PerformanceDTO result = service.updatePerformanceById(existingUserId, pathVariablePerformanceId, requestBodyPerformanceDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(mergedPerformanceDto, result);

        Mockito.verify(userRepository, Mockito.times(1)).findById(existingUserId);
        Mockito.verify(performanceRepository, Mockito.times(1)).findById(pathVariablePerformanceId);
        Mockito.verify(performanceMapper, Mockito.times(1))
                .updatePerformanceEntityFromPerformanceDTO(requestBodyPerformanceDto, performance);
        Mockito.verify(performanceRepository, Mockito.times(1)).save(performance);
        Mockito.verify(performanceMapper, Mockito.times(1)).fromPerformanceEntityToPerformanceDTO(performance);
    }

    @Test
    void givenInvalidUserRole_whenUpdatePerformanceById_thenThrowUserNotAuthorizedException ()
    {
        final Long pathVariablePerformanceId = 10L;
        final String updatedTitle = "Updated Title";

        User testUser = User.builder().id(existingUserId).role(UserRole.USER).build();
        PerformanceDTO requestBodyPerformanceDto = PerformanceDTO.builder().id(pathVariablePerformanceId).title(updatedTitle).build();

        Mockito.when(userRepository.findById(existingUserId)).thenReturn(Optional.of(testUser));

        Assertions.assertThrows(UserNotAuthorizedException.class, () ->
                service.updatePerformanceById(existingUserId, pathVariablePerformanceId, requestBodyPerformanceDto));

        Mockito.verify(userRepository, Mockito.times(1)).findById(existingUserId);
        Mockito.verify(performanceRepository, Mockito.never()).findById(Mockito.anyLong());
        Mockito.verify(performanceRepository, Mockito.never()).save(Mockito.any(Performance.class));
    }

    @Test
    void givenWrongPathVariablePerformanceId_whenUpdatePerformanceById_thenThrowUserNotAuthorizedException ()
    {
        final Long pathVariablePerformanceId = 999L;
        final String updatedTitle = "Updated Title";

        User testUser = User.builder().id(existingUserId).role(UserRole.ADMIN).build();
        PerformanceDTO requestBodyPerformanceDto = PerformanceDTO.builder().id(existingPerformanceId).title(updatedTitle).build();

        Mockito.when(userRepository.findById(existingUserId)).thenReturn(Optional.of(testUser));

        Assertions.assertThrows(PerformanceIdMismatchException.class, () ->
                service.updatePerformanceById(existingUserId, pathVariablePerformanceId, requestBodyPerformanceDto));

        Mockito.verify(userRepository, Mockito.times(1)).findById(existingUserId);
        Mockito.verify(performanceRepository, Mockito.never()).findById(Mockito.anyLong());
        Mockito.verify(performanceRepository, Mockito.never()).save(Mockito.any(Performance.class));
    }

    @Test
    void givenValidKeyword_whenGetSearchTitleSuggestion_thenGetSearchTitleSuggestion ()
    {
        final Long firstPerformanceId = 10L;
        final Long secondPerformanceId = 20L;
        final String firstTitle = "First Test Title";
        final String secondTitle = "Second Test Title";
        final String keyword = "Test Title";

        SearchSuggestion firstSuggestion = Mockito.mock(SearchSuggestion.class);
        Mockito.when(firstSuggestion.getId()).thenReturn(firstPerformanceId);
        Mockito.when(firstSuggestion.getTitle()).thenReturn(firstTitle);
        SearchSuggestion secondSuggestion = Mockito.mock(SearchSuggestion.class);
        Mockito.when(secondSuggestion.getId()).thenReturn(secondPerformanceId);
        Mockito.when(secondSuggestion.getTitle()).thenReturn(secondTitle);

        List<SearchSuggestion> mockSuggestions = List.of(firstSuggestion, secondSuggestion);

        Mockito.when(performanceRepository.searchTitleCompletionSuggestions(keyword))
                .thenReturn(mockSuggestions);

        List<SearchSuggestion> result = service.getSearchTitleSuggestions(keyword);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(firstPerformanceId, result.getFirst().getId());
        Assertions.assertEquals(secondPerformanceId, result.getLast().getId());
        Assertions.assertEquals(firstTitle, result.getFirst().getTitle());
        Assertions.assertEquals(secondTitle, result.getLast().getTitle());

        Mockito.verify(performanceRepository, Mockito.times(1)).searchTitleCompletionSuggestions(keyword);
    }

    @Test
    void givenValidKeyword_whenGetSearchResults_thenGetSearchResults ()
    {
        final String keyword = "Test Title";
        final int pageNumber = 0;
        final int pageSize = 2;
        final Long p1Id = 10L;
        final Long p2Id = 20L;
        final String p1Title = "First Test Title";
        final String p2Title = "Second Test Title";

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Performance p1 = Performance.builder().id(p1Id).title(p1Title).build();
        Performance p2 = Performance.builder().id(p2Id).title(p2Title).build();
        List<Performance> performances = List.of(p1, p2);
        Page<Performance> performancePage = new PageImpl<>(performances, pageable, 2);

        PerformanceCardDTO dto1 = PerformanceCardDTO.builder().id(p1Id).title(p1Title).build();
        PerformanceCardDTO dto2 = PerformanceCardDTO.builder().id(p2Id).title(p2Title).build();

        Mockito.when(performanceRepository.searchProducts(keyword, pageable)).thenReturn(performancePage);
        Mockito.when(performanceMapper.fromPerformanceEntityToPerformanceCardDTO(p1)).thenReturn(dto1);
        Mockito.when(performanceMapper.fromPerformanceEntityToPerformanceCardDTO(p2)).thenReturn(dto2);

        Page<PerformanceCardDTO> result = service.getSearchResults(pageable, keyword);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(pageSize, result.getContent().size());
        Assertions.assertEquals(dto1, result.getContent().getFirst());
        Assertions.assertEquals(dto2, result.getContent().getLast());

        Mockito.verify(performanceRepository, Mockito.times(1)).searchProducts(keyword, pageable);
        Mockito.verify(performanceMapper, Mockito.times(1)).fromPerformanceEntityToPerformanceCardDTO(p1);
        Mockito.verify(performanceMapper, Mockito.times(1)).fromPerformanceEntityToPerformanceCardDTO(p2);
    }
}
