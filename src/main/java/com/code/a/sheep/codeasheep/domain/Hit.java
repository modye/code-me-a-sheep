package com.code.a.sheep.codeasheep.domain;

import lombok.Builder;
import lombok.Data;

/**
 * Hit is a POJO representing a {@link Document} matching a request.
 * It's compound of two fields:
 * <ul>
 * <li>document: the actual document</li>
 * <li>score: the score of the document in the request</li>
 * </ul>
 */
@Data
@Builder
public class Hit implements Comparable<Hit> {
    private final double score;
    private final Document document;

    @Override
    public int compareTo(Hit o) {
        return Double.compare(score, o.getScore());
    }
}
