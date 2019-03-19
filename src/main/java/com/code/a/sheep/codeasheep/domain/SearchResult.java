package com.code.a.sheep.codeasheep.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.List;

/**
 * This POJO represents the result of a search, compound of three fields:
 * <ul>
 * <li>nbHits: the number of total hits matching a request</li>
 * <li>hits: a collection of returned hits matching the request</li>
 * <li>facets: facets, if specified in the request</li>
 * </ul>
 */
@Data
@Builder
public class SearchResult {
    private final long nbHits;
    private final Collection<Hit> hits;
    private final List<Facet> facets;
}