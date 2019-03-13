package com.code.a.sheep.codeasheep.web;

import com.code.a.sheep.codeasheep.domain.SearchResult;
import com.code.a.sheep.codeasheep.interfaces.DocumentSearcher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * This class exposes a REST web service on main path of the server.
 */
@RestController
@RequiredArgsConstructor
public class SearchController {

    private final DocumentSearcher searcher;

    /**
     * Exposes a "/search" GET REST operations that performs a search on the system.
     */
    @GetMapping("/search")
    public ResponseEntity<SearchResult> search(
            @RequestParam(value = "text") @NotEmpty String text,
            @RequestParam(value = "facet.field", required = false) List<String> facetFields) {
        return ResponseEntity.ok().body(searcher.searchDocuments(text, facetFields));
    }
}