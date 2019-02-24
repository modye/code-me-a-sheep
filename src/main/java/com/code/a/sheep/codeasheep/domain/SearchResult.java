package com.code.a.sheep.codeasheep.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchResult {
    private final long nbHits;
    private final List<Hit> hits;
    private List<Facet> facets;
}
