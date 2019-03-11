package com.code.a.sheep.codeasheep.plain.index;

import com.code.a.sheep.codeasheep.domain.Document;
import com.code.a.sheep.codeasheep.domain.Hit;
import com.code.a.sheep.codeasheep.domain.SearchResult;
import com.code.a.sheep.codeasheep.plain.schema.PlainJavaField;
import com.code.a.sheep.codeasheep.plain.schema.PlainJavaSchema;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The main index class.
 * <p>
 * It is a document container with searching structures.
 * <p>
 * TODO: Add synchronization between document addition and flush operation.
 */
public class PlainJavaIndex {
    private final PlainJavaSchema schema;
    private final PlainJavaDocumentStore documentStore;
    private final PlainJavaResultCollector resultCollector;

    public PlainJavaIndex(PlainJavaSchema schema) {
        this.schema = schema;
        documentStore = new PlainJavaDocumentStore();
        resultCollector = new PlainJavaResultCollector(documentStore);
    }

    public void addDocuments(@NotNull List<Document> documents) {
        documents.stream().forEach(this::addDocument);
    }

    private void addDocument(Document document) {
        // store document
        documentStore.addDocument(document);
    }

    // TODO should be synchronized with document addition

    /**
     * Create searching structure for each field and finalize document storage.
     */
    public void commit() {
        Collection<PlainJavaDocument> bufferedDocuments = documentStore.getBufferedDocuments();
        if (bufferedDocuments != null) {
            bufferedDocuments.forEach(document -> {
                // for each field, create searching structures
                document.entrySet().forEach(e -> {
                    PlainJavaField plainJavaField = schema.get(e.getKey());
                    if (plainJavaField != null) {
                        plainJavaField.indexFieldContent(document.getId(), e.getValue(), bufferedDocuments.size());
                    }
                });
            });
        }

        documentStore.commit();

        // finalize searching structures
        schema.entrySet().forEach(entry -> entry.getValue().commit());
    }

    /**
     * Search documents from a simple query.
     * A simple score is computed during
     *
     * @param query given query
     * @param size  number of document to retrieve
     * @return a search result with scored top documents, faceting and total document count
     */
    public SearchResult search(String query, int size) {
        Map<String, String> parsedQuery = parseQuery(query);

        List<PlainJavaPostingList> postingLists = retrievePostingLists(parsedQuery);

        return resultCollector.collectAndComputeScore(postingLists, size);
    }

    /**
     * Main search function which retrieve posting lists from a parsed query.
     *
     * @param parsedQuery query to be used
     * @return a list of posting lists.
     */
    private List<PlainJavaPostingList> retrievePostingLists(Map<String, String> parsedQuery) {
        return parsedQuery.entrySet().stream()
                .map(entry -> {
                    PlainJavaField plainJavaField = schema.get(entry.getKey());
                    if (plainJavaField != null) {
                        return plainJavaField.searchDocuments(entry.getValue());
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * Parse simple queries like 'field1:word1 field2:word2'
     *
     * @param query the given query
     * @return a query in the form of {@link HashMap}
     */
    private Map<String, String> parseQuery(String query) {
        String[] clauses = query.split(" ");

        return Arrays.stream(clauses)
                .map(clause -> clause.split(":"))
                .collect(Collectors.toMap(clause -> clause[0], clause -> clause[1]));
    }
}
