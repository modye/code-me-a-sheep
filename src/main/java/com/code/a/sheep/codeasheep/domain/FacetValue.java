package com.code.a.sheep.codeasheep.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FacetValue {
    private final Object key;
    private final int count;
}
