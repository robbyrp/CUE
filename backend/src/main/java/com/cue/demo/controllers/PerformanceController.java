package com.cue.demo.controllers;

import com.cue.demo.dtos.performance.PerformanceDTO;
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
@RequestMapping("/api/spectacole")
@CrossOrigin(origins = "http://localhost:5174")
public final class PerformanceController {

    private final PerformanceService service;

    public PerformanceController(PerformanceService service) {
        this.service = service;
    }

    @PostMapping("/admin")
    public ResponseEntity<Void> createPerformance(
            @RequestBody @Valid final PerformanceDTO dto) {

        service.createPerformance(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/")
    public ResponseEntity<Page<PerformanceDTO>> getPerformances(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) final Pageable pageable) {

        Page<PerformanceDTO> page = service.getPerformances(pageable);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerformanceDTO> getPerformanceById(@PathVariable final Long id) {
        PerformanceDTO dto = service.getPerformanceById(id);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}/admin")
    public ResponseEntity<Void> deletePerformanceById(
            @PathVariable final Long id) {

        service.deletePerformanceById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/admin")
    public ResponseEntity<PerformanceDTO> updatePerformanceById(
            @PathVariable final Long id,
            @RequestBody @Valid final PerformanceDTO dto) {
        PerformanceDTO updatedTo = service.updatePerformanceById(id, dto);
        return ResponseEntity.ok().body(updatedTo);
    }

}
