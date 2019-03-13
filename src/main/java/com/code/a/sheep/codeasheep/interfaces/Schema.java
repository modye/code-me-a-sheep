package com.code.a.sheep.codeasheep.interfaces;

import java.util.HashMap;

/**
 * This class represents the schema, the structure of our documents in the system.
 * It's a simple {@link HashMap<String, T>}
 *
 * @param <T>
 */
public class Schema<T> extends HashMap<String, T> {

    /**
     * Add a new field in the schema
     */
    public Schema<T> addField(String name, T field) {
        this.put(name, field);
        return this;
    }
}
