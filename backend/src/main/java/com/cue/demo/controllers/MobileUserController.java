package com.cue.demo.controllers;

import com.cue.demo.dtos.WatchPerformanceItemDTO;
import com.cue.demo.services.MobileUserService;
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
                                               @RequestHeader(value="X-User-Id", defaultValue="1") final Long userId) {
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(userId, performanceId);
        service.addItemToWatchLater(dto);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/me/watch-later/{performanceId}")
    public ResponseEntity<Void> deleteItemFromWatchLater(@PathVariable final Long performanceId,
                                                        @RequestHeader(value="X-User-Id",defaultValue="1") final Long userId) {
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(userId, performanceId);
        service.deleteItemFromWatchLater(dto);
        return ResponseEntity.noContent().build();
    }
}
