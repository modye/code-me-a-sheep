package com.code.a.sheep.codeasheep.plain.schema;

import com.code.a.sheep.codeasheep.plain.index.PlainJavaFieldIndex;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PlainJavaField {
    private final String name;
    private final PlainJavaFieldIndex invertedIndex;

    // TODO add columnar storage

    public void indexDocument(int documentId, Object fieldValue) {
        invertedIndex.indexDocument(documentId, fieldValue);
    }
}
