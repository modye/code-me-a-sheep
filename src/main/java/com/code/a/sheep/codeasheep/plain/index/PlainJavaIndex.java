package com.code.a.sheep.codeasheep.plain.index;

import com.code.a.sheep.codeasheep.domain.Document;
import com.code.a.sheep.codeasheep.domain.SearchResult;
import com.code.a.sheep.codeasheep.plain.schema.PlainJavaField;
import com.code.a.sheep.codeasheep.plain.schema.PlainJavaSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static com.code.a.sheep.codeasheep.domain.DocumentFields.TEXT;

/**
 * The main index class.
 * <p>
 * It is a document container with searching structures.
 * <p>
 */
@Component
@RequiredArgsConstructor
public class PlainJavaIndex {
    private final PlainJavaSchema schema;
    private PlainJavaDocumentStore documentStore;
    private PlainJavaResultCollector resultCollector;

    @PostConstruct
    public void init() {
        documentStore = new PlainJavaDocumentStore();
        resultCollector = new PlainJavaResultCollector(documentStore, schema);
    }

    public void addDocuments(@NotNull List<Document> documents) {
        documents.forEach(document -> {
            // store document
            documentStore.addDocument(document);
        });
    }

    // TODO should be synchronized with document addition

    /**
     * Create searching structure for each field and finalize document storage.
     */
    public void commit() {
        Collection<PlainJavaDocument> bufferedDocuments = documentStore.getBufferedDocuments();
        if (bufferedDocuments != null) {
            bufferedDocuments.forEach(document -> {
                // TODO-03-a Call a method to create search structures

            });
        }

        documentStore.commit();

        // finalize searching structures
        schema.forEach((key, value) -> value.commit());
    }

    private void createSearchStructuresForDocument(PlainJavaDocument document) {
        document.forEach((key, value) -> {
            PlainJavaField plainJavaField = schema.get(key);
            if (plainJavaField != null) {
                plainJavaField.setColumnarStorageSize(documentStore.getNextDocumentId());
                plainJavaField.indexFieldContent(document.getId(), value.toString());
            }
        });
    }

    /**
     * Search documents from a simple query.
     * A simple score is computed during
     *
     * @param query       given query
     * @param facetFields facet fields
     * @param size        number of document to retrieve
     * @return a search result with scored top documents, faceting and total document count
     */
    public SearchResult search(String query, List<String> facetFields, int size) {
        Map<String, String> parsedQuery = parseQuery(query);

        List<PlainJavaPostingList.PostingIterator> postingLists = retrievePostingLists(parsedQuery);

        SearchResult.SearchResultBuilder builder = SearchResult.builder();

        List<Integer> matchingDocumentIds = resultCollector.collectDocsAndComputeScores(builder, postingLists, size);
        // TODO-08-b Collect the facets and add them to the builder

        return builder.build();
    }

    /**
     * Main search function which retrieve posting lists from a parsed query.
     *
     * @param parsedQuery query to be used
     * @return a list of posting lists.
     */
    private List<PlainJavaPostingList.PostingIterator> retrievePostingLists(Map<String, String> parsedQuery) {
        return parsedQuery.entrySet().stream()
                .map(entry -> {
                    PlainJavaField plainJavaField = schema.get(entry.getKey());
                    if (plainJavaField != null) {
                        List<PlainJavaPostingList.PostingIterator> postingLists = plainJavaField.searchDocuments(entry.getValue())
                                .stream()
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
                        postingLists.forEach(p -> p.setSearchedField(entry.getKey()));
                        return postingLists;
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * Ugly parsing of simple queries like 'field1:word1 field2:word2'
     *
     * @param query the given query
     * @return a query in the form of {@link HashMap}
     */
    private Map<String, String> parseQuery(String query) {
        String[] clauses = query.split(" ");

        Map<String, String> queryMap = new HashMap<>();

        try {
            Arrays.stream(clauses)
                    .map(clause -> clause.split(":"))
                    .forEach(clause -> {
                        String fieldName = getFieldName(clause);
                        String queryText = getQueryText(clause);

                        queryMap.compute(fieldName, (field, text) -> {
                            if (text == null) {
                                text = queryText;
                            } else {
                                text += " " + queryText;
                            }

                            return text;
                        });
                    });

            return queryMap;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Error while parsing query: '" + query + "'");
        }
    }

    private String getQueryText(String[] clause) {
        return clause.length == 1 ? clause[0] : clause[1];
    }

    private String getFieldName(String[] clause) {
        return clause.length == 1 ? TEXT.getName() : clause[0];
    }
}
