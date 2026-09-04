package com.cue.demo.controllers;

import com.cue.demo.dtos.auth.LoginRequestDTO;
import com.cue.demo.dtos.auth.LoginResponseDTO;
import com.cue.demo.dtos.auth.RegisterUserRequestDTO;
import com.cue.demo.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
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

}
