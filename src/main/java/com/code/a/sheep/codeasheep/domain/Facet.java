package com.code.a.sheep.codeasheep.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Facet {
    private final String field;
    private final List<FacetValue> values;
}
