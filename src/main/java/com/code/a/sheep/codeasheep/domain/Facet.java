package com.code.a.sheep.codeasheep.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Facet {
    private final String key;
    private final int count;
}
