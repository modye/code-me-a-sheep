package com.code.a.sheep.codeasheep.plain.index;

import com.code.a.sheep.codeasheep.plain.schema.PlainJavaField;
import com.code.a.sheep.codeasheep.plain.schema.PlainJavaSchema;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The main index class.
 * <p>
 * It is a document container with searching structures.
 */
public class PlainJavaIndex extends HashMap<Integer, Map<String, Object>> {
    private final PlainJavaSchema schema;
    private final AtomicInteger nextDocumentId;


    public PlainJavaIndex(PlainJavaSchema schema) {
        this.schema = schema;
        this.nextDocumentId = new AtomicInteger();
    }

    public void addDocument(Map<String, Object> document) {
        int documentId = nextDocumentId.getAndDecrement();

        // store document
        this.put(documentId, document);

        // for each field, create searching structures
        document.entrySet().stream().forEach(e -> {
            PlainJavaField plainJavaField = schema.get(e.getKey());
            if (plainJavaField != null) {
                plainJavaField.indexDocument(documentId, e.getValue());
            }
        });
    }

    public PlainJavaPostingList search(String query) {
        return null;
    }
}
