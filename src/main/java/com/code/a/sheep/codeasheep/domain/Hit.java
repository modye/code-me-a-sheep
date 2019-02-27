package com.code.a.sheep.codeasheep.domain;

import lombok.Data;

import java.util.Map;

@Data
public class Hit {
    private final float score;
    private final Map<String, Object> document;
}
