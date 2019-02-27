package com.code.a.sheep.codeasheep.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class SearchResult {
    private final long nbHits;
    private final List<Hit> hits;
    private final List<Facet> facets;
}
