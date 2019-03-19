package com.code.a.sheep.codeasheep.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * This POJO represents our domain entity for a Document.
 * This is a simple key/value {@link HashMap<String, Object>} having:
 * <ul>
 * <li>key: field of the document</li>
 * <li>value: value of the field</li>
 * </ul>
 */
public class Document extends HashMap<String, Object> {

    public Document() {
    }

    public Document(Map<? extends String, ?> m) {
        super(m);
    }
}
