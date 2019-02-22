package com.code.a.sheep.codeasheep.domain;

import lombok.Data;

import java.util.List;

@Data
public class SearchResult {
    private final long nbHits;
    private final List<Hit> hits;
    private List<Facet> facets;
}
