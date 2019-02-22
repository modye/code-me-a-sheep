package com.code.a.sheep.codeasheep;

import com.code.a.sheep.codeasheep.domain.SearchResult;

import javax.validation.constraints.NotNull;

public interface DocumentSearcher {
    SearchResult searchDocuments(@NotNull String query);
}
