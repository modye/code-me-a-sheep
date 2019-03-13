package com.code.a.sheep.codeasheep.interfaces;

import com.code.a.sheep.codeasheep.domain.SearchResult;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Main interface for search operations
 */
public interface DocumentSearcher {

    /**
     * Search for documents.
     *
     * @param query       query to execute
     * @param facetFields execute facets on those fields
     */
    SearchResult searchDocuments(@NotNull String query, List<String> facetFields);

    /**
     * Initializes the index searcher
     */
    void initializeIndexSearcher();
}
