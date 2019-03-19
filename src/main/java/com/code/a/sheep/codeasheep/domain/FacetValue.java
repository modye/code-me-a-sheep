package com.code.a.sheep.codeasheep.domain;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * A POJO representing one result of a facet:
 * <ul>
 * <li>key: key of the bucket</li>
 * <li>count: actual count of the bucket</li>
 * </ul>
 */
@Data
@Builder
@RequiredArgsConstructor
public class FacetValue {
    private final Object key;
    private final int count;
}
