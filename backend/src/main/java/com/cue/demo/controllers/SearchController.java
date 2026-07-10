package com.cue.demo.controllers;

import com.cue.demo.dtos.PerformanceCardDTO;
import com.cue.demo.interfaces.SearchSuggestion;
import com.cue.demo.services.PerformancePortalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final PerformancePortalService service;

    public SearchController(final PerformancePortalService service) {
        this.service = service;
    }

    /**
     * Endpoint for title suggestions. Should be called whenever a user has
     * a 300ms pause while typing in the search bar.
     * @param q query
     * @return Returns a list of the top 6 suggestions (title-id pairs) by relevance.
     */
    @GetMapping("/suggestions")
    public ResponseEntity<List<SearchSuggestion>> getSearchTitleSuggestions(@RequestParam String q) {
        List<SearchSuggestion> suggestions = service.getSearchTitleSuggestions(q);
        return ResponseEntity.ok().body(suggestions);
    }

    @GetMapping("/performances")
    public ResponseEntity<Page<PerformanceCardDTO>> getSearchResults(
            @PageableDefault(sort="id", direction=Sort.Direction.ASC) Pageable pageable,
            @RequestParam String q) {

        Page<PerformanceCardDTO> page = service.getSearchResults(pageable, q);
        return ResponseEntity.ok().body(page);
    }
}
