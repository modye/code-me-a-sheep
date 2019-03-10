package com.code.a.sheep.codeasheep;

import java.util.HashMap;

public class Schema<T> extends HashMap<String, T> {
    public Schema<T> addField(String name, T field) {
        this.put(name, field);

        return this;
    }
}
