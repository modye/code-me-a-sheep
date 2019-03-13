package com.code.a.sheep.codeasheep.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * A POJO representing a facet result:
 * <ul>
 * <li>field: on which field the facet occured</li>
 * <li>values: a list of {@link FacetValue}</li>
 * </ul>
 */
@Data
@Builder
public class Facet {
    private final String field;
    private final List<FacetValue> values;
}