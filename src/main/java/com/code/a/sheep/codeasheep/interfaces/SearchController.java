package com.code.a.sheep.codeasheep.interfaces;

import com.code.a.sheep.codeasheep.DocumentSearcher;
import com.code.a.sheep.codeasheep.domain.SearchResult;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.TopDocs;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final DocumentSearcher searcher;

    @RequestMapping("/search")
    public ResponseEntity<SearchResult> search(@RequestParam("text") String text, @RequestParam(value = "facet.field", required = false) List<String> facetFields) {
        return ResponseEntity.ok().body(searcher.searchDocuments(text, facetFields));
    }
}



