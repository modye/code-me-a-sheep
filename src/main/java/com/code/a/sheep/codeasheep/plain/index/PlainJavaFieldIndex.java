package com.code.a.sheep.codeasheep.plain.index;

import java.util.HashMap;
import java.util.Map;

public class PlainJavaFieldIndex {
    /**
     * This map is a fast access for fulltext searches
     */
    private final Map<Object, PlainJavaPostingList> invertedIndex;

    public PlainJavaFieldIndex() {
        invertedIndex = new HashMap<>();
    }

    /**
     * Add a document id for a given token in the posting list.
     *
     * @param documentId document id
     * @param token      token obtained by document field analysis
     */
    public void indexDocument(int documentId, Object token) {

        // Use inverted index for fulltext search purposes
        invertedIndex.computeIfAbsent(token, k -> new PlainJavaPostingList()).add(documentId);
    }

    /**
     * Search documents containing this given token
     *
     * @param token token obtained by document field analysis
     * @return the posting list or null if not found
     */
    public PlainJavaPostingList searchDocument(Object token) {
        return invertedIndex.get(token);
    }
}
