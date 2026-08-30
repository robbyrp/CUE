package com.cue.demo.controllers;

import com.cue.demo.dtos.*;
import com.cue.demo.security.UserSecurityAdapter;
import com.cue.demo.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5174")
public final class AuthController {
    private final AuthService service;

    public AuthController(final AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody @Valid LoginRequestDTO request) {
        LoginResponseDTO response = service.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody @Valid RegisterUserRequestDTO request) {
        LoginResponseDTO response = service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getCurrentUser (@AuthenticationPrincipal UserSecurityAdapter principal) {
        UserProfileDTO profile = service.getCurrentUserProfileById(principal.getId());
        return ResponseEntity.status(HttpStatus.OK).body(profile);
    }

}
