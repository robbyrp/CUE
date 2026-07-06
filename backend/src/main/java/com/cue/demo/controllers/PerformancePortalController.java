package com.cue.demo.controllers;

import com.cue.demo.dtos.PerformancePortalDTO;
import com.cue.demo.services.PerformancePortalService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class PerformancePortalController {

    private final PerformancePortalService service;

    public PerformancePortalController(PerformancePortalService service) {
        this.service = service;
    }


    @PostMapping("/create")
    public ResponseEntity<Void> createPerformance(
            @RequestBody @Valid final PerformancePortalDTO dto) {
        service.createPerformance(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/spectacole")
    public ResponseEntity<Page<PerformancePortalDTO>> getPerformances(final Pageable pageable) {
        Page<PerformancePortalDTO> page = service.getPerformances(pageable);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/spectacole/{id}")
    public ResponseEntity<PerformancePortalDTO> getPerformanceById(@PathVariable final Long id) {
        PerformancePortalDTO dto = service.getPerformanceById(id);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/spectacole/{id}")
    public ResponseEntity<Void> deletePerformanceById(@PathVariable final Long id) {
        service.deletePerformanceById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/spectacole/{id}")
    public ResponseEntity<PerformancePortalDTO> updatePerformanceById(
            @PathVariable final Long id,
            @RequestBody @Valid final PerformancePortalDTO dto) {
        PerformancePortalDTO updatedTo = service.updatePerformanceById(id, dto);
        return ResponseEntity.ok().body(updatedTo);
    }
}
