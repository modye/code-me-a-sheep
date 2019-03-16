package com.code.a.sheep.codeasheep.plain.schema;

import com.code.a.sheep.codeasheep.plain.index.PlainJavaFieldIndex;
import com.code.a.sheep.codeasheep.plain.index.PlainJavaPostingList;
import com.code.a.sheep.codeasheep.plain.index.TextAnalysis;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PlainJavaField {
    private final String name;
    private boolean withColumnarStorage;


    private final PlainJavaFieldIndex fieldIndex;

    @Builder
    public PlainJavaField(String name, boolean withColumnarStorage) {
        this.name = name;
        this.withColumnarStorage = withColumnarStorage;
        this.fieldIndex = new PlainJavaFieldIndex(withColumnarStorage);
    }

    /**
     * Create indexing structures.
     * documentCount can be used for storage optimization.
     *
     * @param documentId document storage document id
     * @param content    field content
     */
    //TODO-04-c Replace previous analysis with the one you just created
    public void indexFieldContent(int documentId, String content) {
        // Call analysis and add tokens to the inverted index
        //TODO-03-c Add the tokens in the field index for this content
        fieldIndex.addToken(documentId, TextAnalysis.defaultAndLowerCaseAnalysis(content), content);
    }

    /**
     * Search document with a given query content.
     *
     * @param query content to retrieve
     * @return the result posting lists
     */
    public List<PlainJavaPostingList.PostingIterator> searchDocuments(String query) {
        // TODO-06-c Replace this empty List.of() with: Analyze the incoming query and search documents
        return TextAnalysis.defaultAnalysis(query)
                .stream()
                .map(fieldIndex::searchDocument)
                .collect(Collectors.toList());
        //return List.of();
    }

    /**
     * Finalize searching structures
     */
    public void commit() {
        fieldIndex.commit();
    }

    public Object getTokenForDocument(int documentId) {
        if (!withColumnarStorage) {
            throw new IllegalArgumentException("Field '" + name + "' does not contain columnar storage");
        }

        return fieldIndex.getTokenForDocument(documentId);
    }

    public void setColumnarStorageSize(int size) {
        fieldIndex.setColumnarStorageSize(size);
    }
}
