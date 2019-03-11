package com.code.a.sheep.codeasheep.plain.schema;

import com.code.a.sheep.codeasheep.plain.index.PlainJavaFieldIndex;
import com.code.a.sheep.codeasheep.plain.index.PlainJavaPostingList;
import com.code.a.sheep.codeasheep.plain.index.TextAnalysis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Data
@Builder
public class PlainJavaField {
    private final String name;
    private final PlainJavaFieldIndex invertedIndex = new PlainJavaFieldIndex();

    // TODO add columnar storage

    /**
     * Create indexing structures.
     * documentCount can be used for storage optimization.
     *
     * @param documentId    document storage document id
     * @param content       field content
     * @param documentCount total document count in
     */
    public void indexFieldContent(int documentId, Object content, int documentCount) {
        // Call analysis
        TextAnalysis.defaultAnalysis(content.toString())
                .stream()
                .forEach(token -> invertedIndex.indexDocument(documentId, token));
    }

    /**
     * Search document with a given query content.
     * @param query content to retrieve
     * @return the result posting lists
     */
    public List<PlainJavaPostingList> searchDocuments(String query) {
        return TextAnalysis.defaultAnalysis(query)
                .stream()
                .map(invertedIndex::searchDocument)
                .collect(Collectors.toList());
    }

    /**
     * Finalize searching structures
     */
    public void commit() {
        invertedIndex.commit();
    }
}
