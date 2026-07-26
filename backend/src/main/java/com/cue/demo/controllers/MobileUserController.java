package com.cue.demo.controllers;

import com.cue.demo.dtos.PerformanceCardDTO;
import com.cue.demo.dtos.WatchPerformanceItemDTO;
import com.cue.demo.entities.Performance;
import com.cue.demo.services.MobileUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mobile/users")
public final class MobileUserController {
    private final MobileUserService service;

    public MobileUserController(final MobileUserService service) {
        this.service = service;
    }

    //POST, DELETE, GETALL (PAGEABLE)

    //-------------WATCH LATER------------------
    @PostMapping("/me/watch-later/{performanceId}")
    public ResponseEntity<Void> addItemToWatchLater(@PathVariable final Long performanceId,
                                               @RequestHeader(value = "X-User-Id", defaultValue = "1") final Long userId) {
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(userId, performanceId);
        service.addItemToWatchLater(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/watch-later/{performanceId}")
    public ResponseEntity<Void> deleteItemFromWatchLater(@PathVariable final Long performanceId,
                                                        @RequestHeader(value = "X-User-Id", defaultValue = "1") final Long userId) {
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(userId, performanceId);
        service.deleteItemFromWatchLater(dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/watch-later/items")
    public ResponseEntity<Page<PerformanceCardDTO>> getWatchLaterPerformanceCards(
            @RequestHeader(value = "X-User-Id", defaultValue = "1") final Long userId,
            @PageableDefault(sort = "addedAtTime", direction = Sort.Direction.ASC) final Pageable pageable) {

        Page<PerformanceCardDTO> page = service.getWatchLaterPerformanceCards(pageable, userId);
        return ResponseEntity.ok().body(page);
    }

    //-------------WATCHED---------------------------
    @PostMapping("/me/watched/{performanceId}")
    public ResponseEntity<Void> addItemToWatched(@PathVariable Long performanceId,
                                                 @RequestHeader(value = "X-User-Id", defaultValue = "1") final Long userId) {
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(userId, performanceId);
         service.addItemToWatched(dto);
         return ResponseEntity.ok().build();
    }

    @DeleteMapping("me/watched/{performanceId}")
    public ResponseEntity<Void> deleteItemFromWatched(@PathVariable Long performanceId,
                                                      @RequestHeader(value = "X-User-Id", defaultValue = "1") final Long userId) {
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(userId, performanceId);
        service.deleteItemFromWatched(dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("me/watched/items")
    public ResponseEntity<Page<PerformanceCardDTO>> getWatchedPerformanceItems(
            @RequestHeader(value = "X-User-Id", defaultValue = "1") final Long userId,
            @PageableDefault(sort = "watchedAtTime", direction = Sort.Direction.ASC) final Pageable pageable) {

        Page<PerformanceCardDTO> page = service.getWatchedPerformanceCards(pageable, userId);
        return ResponseEntity.ok().body(page);
    }
}