package com.code.a.sheep.codeasheep;

import com.code.a.sheep.codeasheep.domain.SearchResult;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface DocumentSearcher {
    SearchResult searchDocuments(@NotNull String query, List<String> facetFields);
}
