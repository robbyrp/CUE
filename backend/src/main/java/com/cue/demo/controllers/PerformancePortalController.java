package com.cue.demo.controllers;

import com.cue.demo.dtos.PerformancePortalDTO;
import com.cue.demo.services.PerformancePortalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/performances")
public class PerformancePortalController {

    private final PerformancePortalService service;

    public PerformancePortalController(PerformancePortalService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> createPerformance(
            @RequestBody @Valid
            PerformancePortalDTO performancePortalDTO) {
        service.createPerformance(performancePortalDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
