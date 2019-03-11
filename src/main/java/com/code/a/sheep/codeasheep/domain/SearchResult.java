package com.code.a.sheep.codeasheep.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.List;


@Data
@Builder
public class SearchResult {
    private final long nbHits;
    private final Collection<Hit> hits;
    private final List<Facet> facets;
}
