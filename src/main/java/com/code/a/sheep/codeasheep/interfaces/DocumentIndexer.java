package com.code.a.sheep.codeasheep.interfaces;

import com.code.a.sheep.codeasheep.domain.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Index a list of documents, represented as a Map<String, Object> in the underlying layers.
 */
public interface DocumentIndexer {

    /**
     * Index all documents in the system
     *
     * @param documents a List of Map<String, Object> that represents documents
     */
    void indexDocuments(@NotNull List<Document> documents);

    /**
     * Commit documents and create indexing structures in order to make documents searchable
     */
    void commit();
}
