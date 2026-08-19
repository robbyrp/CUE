package com.cue.demo.controllers;

import com.cue.demo.dtos.PerformanceCardDTO;
import com.cue.demo.dtos.WatchPerformanceItemDTO;
import com.cue.demo.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5174")
public final class UserController {
    private final UserService service;

    public UserController(final UserService service) {
        this.service = service;
    }

    //-------------WATCH LATER------------------
    @PostMapping("/me/watch-later/{performanceId}")
    public ResponseEntity<Void> addItemToWatchLater(@PathVariable final Long performanceId,
                                               @RequestHeader(value = "X-User-Id") final Long userId) {
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(userId, performanceId);
        service.addItemToWatchLater(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/me/watch-later/{performanceId}")
    public ResponseEntity<Void> deleteItemFromWatchLater(@PathVariable final Long performanceId,
                                                        @RequestHeader(value = "X-User-Id") final Long userId) {
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(userId, performanceId);
        service.deleteItemFromWatchLater(dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/watch-later")
    public ResponseEntity<Page<PerformanceCardDTO>> getWatchLaterPerformanceCards(
            @RequestHeader(value = "X-User-Id") final Long userId,
            @PageableDefault(sort = "addedAtTime", direction = Sort.Direction.ASC) final Pageable pageable) {

        Page<PerformanceCardDTO> page = service.getWatchLaterPerformanceCards(pageable, userId);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/me/watch-later/{performanceId}/exists")
    public ResponseEntity<Boolean> isPerformanceInWatchLater(@PathVariable final Long performanceId,
                                                             @RequestHeader(value = "X-User-Id") final Long userId) {
        Boolean exists = service.isPerformanceInWatchLater(userId, performanceId);
        return ResponseEntity.ok().body(exists);
    }

    //-------------WATCHED---------------------------
    @PostMapping("/me/watched/{performanceId}")
    public ResponseEntity<Void> addItemToWatched(@PathVariable Long performanceId,
                                                 @RequestHeader(value = "X-User-Id") final Long userId) {
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(userId, performanceId);
         service.addItemToWatched(dto);
         return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("me/watched/{performanceId}")
    public ResponseEntity<Void> deleteItemFromWatched(@PathVariable Long performanceId,
                                                      @RequestHeader(value = "X-User-Id") final Long userId) {
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(userId, performanceId);
        service.deleteItemFromWatched(dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("me/watched")
    public ResponseEntity<Page<PerformanceCardDTO>> getWatchedPerformanceItems(
            @RequestHeader(value = "X-User-Id") final Long userId,
            @PageableDefault(sort = "watchedAtTime", direction = Sort.Direction.ASC) final Pageable pageable) {

        Page<PerformanceCardDTO> page = service.getWatchedPerformanceCards(pageable, userId);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/me/watched/{performanceId}/exists")
    public ResponseEntity<Boolean> isPerformanceInWatched(@PathVariable final Long performanceId,
                                                             @RequestHeader(value = "X-User-Id") final Long userId) {
        Boolean exists = service.isPerformanceInWatched(userId, performanceId);
        return ResponseEntity.ok().body(exists);
    }

    //-------------REVIEWED---------------------------
    //TODO: Add enpoint that gets all performances that were reviewed by user identified by x-user-id request header

}
