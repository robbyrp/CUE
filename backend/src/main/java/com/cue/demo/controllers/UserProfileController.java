package com.cue.demo.controllers;

import com.cue.demo.dtos.profile.UserProfileDTO;
import com.cue.demo.dtos.profile.UserProfileUpdateRequestDTO;
import com.cue.demo.security.UserSecurityAdapter;
import com.cue.demo.services.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:5174")

public class UserProfileController {

    private final UserProfileService service;

    public UserProfileController(final UserProfileService service) {this.service = service;}

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getCurrentUser (@AuthenticationPrincipal UserSecurityAdapter principal) {
        UserProfileDTO profile = service.getCurrentUserProfileById(principal.getId());
        return ResponseEntity.status(HttpStatus.OK).body(profile);
    }

    @PutMapping("/me/update")
    public ResponseEntity<UserProfileDTO> updateCurrentUserProfile(@AuthenticationPrincipal UserSecurityAdapter principal,
                                                                   @RequestBody @Valid UserProfileUpdateRequestDTO request) {

        final Long userId = principal.getId();
        UserProfileDTO updated = service.updateCurrentUserProfile(userId, request);
        return ResponseEntity.status(HttpStatus.OK).body(updated);

    }
}
