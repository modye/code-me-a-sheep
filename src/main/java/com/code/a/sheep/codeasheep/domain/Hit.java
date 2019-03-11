package com.code.a.sheep.codeasheep.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Hit implements Comparable<Hit>{
    private final float score;
    private final Document document;

    @Override
    public int compareTo(Hit o) {
        return Float.compare(score, o.getScore());
    }
}
