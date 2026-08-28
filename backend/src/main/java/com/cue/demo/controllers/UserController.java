package com.cue.demo.controllers;

import com.cue.demo.dtos.PerformanceCardDTO;
import com.cue.demo.dtos.WatchPerformanceItemDTO;
import com.cue.demo.security.UserSecurityAdapter;
import com.cue.demo.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
                                                    @AuthenticationPrincipal UserSecurityAdapter principal) {

        final Long userId = principal.getId();
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(userId, performanceId);
        service.addItemToWatchLater(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/me/watch-later/{performanceId}")
    public ResponseEntity<Void> deleteItemFromWatchLater(@PathVariable final Long performanceId,
                                                         @AuthenticationPrincipal UserSecurityAdapter principal) {

        final Long userId = principal.getId();
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(userId, performanceId);
        service.deleteItemFromWatchLater(dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/watch-later")
    public ResponseEntity<Page<PerformanceCardDTO>> getWatchLaterPerformanceCards(
            @AuthenticationPrincipal UserSecurityAdapter principal,
            @PageableDefault(sort = "addedAtTime", direction = Sort.Direction.ASC) final Pageable pageable) {

        final Long  userId = principal.getId();
        Page<PerformanceCardDTO> page = service.getWatchLaterPerformanceCards(userId, pageable);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/me/watch-later/{performanceId}/exists")
    public ResponseEntity<Boolean> isPerformanceInWatchLater(@PathVariable final Long performanceId,
                                                             @AuthenticationPrincipal UserSecurityAdapter principal) {

        final Long userId = principal.getId();
        Boolean exists = service.isPerformanceInWatchLater(userId, performanceId);
        return ResponseEntity.ok().body(exists);
    }

    //-------------WATCHED---------------------------
    @PostMapping("/me/watched/{performanceId}")
    public ResponseEntity<Void> addItemToWatched(@PathVariable Long performanceId,
                                                 @AuthenticationPrincipal UserSecurityAdapter principal) {

        final Long userId = principal.getId();
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(userId, performanceId);
         service.addItemToWatched(dto);
         return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("me/watched/{performanceId}")
    public ResponseEntity<Void> deleteItemFromWatched(@PathVariable Long performanceId,
                                                      @AuthenticationPrincipal UserSecurityAdapter principal) {

        final Long userId = principal.getId();
        WatchPerformanceItemDTO dto = new WatchPerformanceItemDTO(userId, performanceId);
        service.deleteItemFromWatched(dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("me/watched")
    public ResponseEntity<Page<PerformanceCardDTO>> getWatchedPerformanceItems(
            @AuthenticationPrincipal UserSecurityAdapter principal,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) final Pageable pageable) {

        final Long  userId = principal.getId();
        Page<PerformanceCardDTO> page = service.getWatchedPerformanceCards(userId, pageable);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/me/watched/{performanceId}/exists")
    public ResponseEntity<Boolean> isPerformanceInWatched(@PathVariable final Long performanceId,
                                                          @AuthenticationPrincipal UserSecurityAdapter principal) {

        final Long userId = principal.getId();
        Boolean exists = service.isPerformanceInWatched(userId, performanceId);
        return ResponseEntity.ok().body(exists);
    }

    //-------------REVIEWED---------------------------
    @GetMapping("/me/reviewed")
    public ResponseEntity<Page<PerformanceCardDTO>> getReviewedPerformanceCards(
            @AuthenticationPrincipal UserSecurityAdapter principal,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) final Pageable pageable) {

        final Long  userId = principal.getId();
        Page<PerformanceCardDTO> reviewed = service.getReviewedPerformanceCards(userId, pageable);
        return ResponseEntity.ok().body(reviewed);
    }
}
