package com.cue.demo.controllers;

import com.cue.demo.dtos.PerformanceDTO;
import com.cue.demo.services.PerformanceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5174")
public final class PerformanceController {

    private final PerformanceService service;

    public PerformanceController(PerformanceService service) {
        this.service = service;
    }

    @PostMapping("/spectacole")
    public ResponseEntity<Void> createPerformance(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestBody @Valid final PerformanceDTO dto) {

        service.createPerformance(userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/spectacole")
    public ResponseEntity<Page<PerformanceDTO>> getPerformances(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) final Pageable pageable) {

        Page<PerformanceDTO> page = service.getPerformances(pageable);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/spectacole/{id}")
    public ResponseEntity<PerformanceDTO> getPerformanceById(@PathVariable final Long id) {
        PerformanceDTO dto = service.getPerformanceById(id);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/spectacole/{id}")
    public ResponseEntity<Void> deletePerformanceById(
            @RequestHeader(value = "X-User-Id") Long userId,
            @PathVariable final Long id) {

        service.deletePerformanceById(userId, id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/spectacole/{id}")
    public ResponseEntity<PerformanceDTO> updatePerformanceById(
            @RequestHeader(value = "X-User-Id") Long userId,
            @PathVariable final Long id,
            @RequestBody @Valid final PerformanceDTO dto) {
        PerformanceDTO updatedTo = service.updatePerformanceById(userId, id, dto);
        return ResponseEntity.ok().body(updatedTo);
    }

}
