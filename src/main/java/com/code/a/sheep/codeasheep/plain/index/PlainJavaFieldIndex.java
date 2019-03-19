package com.code.a.sheep.codeasheep.plain.index;

import java.util.*;

public class PlainJavaFieldIndex {
    /**
     * This map is a fast access for fulltext searches
     */
    private final Map<Object, PlainJavaPostingList> invertedIndex;

    private final boolean withColumnarStorage;

    private Object[] columnarStorage;

    private SortedMap<Integer, Integer> temporaryTokenCountPerDocument;
    private int[] tokenCountPerDocument;
    private boolean isCommitted;

    public PlainJavaFieldIndex(boolean withColumnarStorage) {
        this.withColumnarStorage = withColumnarStorage;

        // Sorted map allows range searches
        invertedIndex = new TreeMap<>();
        // We need the biggest document id during commit process, that's why we use a sorted map.
        temporaryTokenCountPerDocument = new TreeMap<>();
        isCommitted = false;
    }

    /**
     * Add a document id for a given token in the posting list.
     *
     * @param documentId document id
     * @param tokens     tokens obtained by document field analysis
     * @param value      field value for columnar storage
     */
    public void addToken(int documentId, List<String> tokens, Object value) {
        // Use inverted index for fulltext search purposes
        // TODO-03-d For each token, add it to posting list
        tokens.forEach(token -> addTokenToPostingList(documentId, token));

        if (withColumnarStorage) {
            //TODO-05-b Update columnar storage for this document and this value
            columnarStorage[documentId] = value;
        }

        incrementsTokenCountForDocument(documentId, tokens.size());
    }

    public int getTokenCountForDocument(int documentId) {
        return isCommitted ? tokenCountPerDocument[documentId] : 0;
    }

    /**
     * Token counts are used in scoring in order to boost shorter documents.
     *
     * @param documentId given document id
     * @param count      increment
     */
    private void incrementsTokenCountForDocument(int documentId, int count) {
        temporaryTokenCountPerDocument.compute(documentId, (id, tokenCount) -> {
            if (tokenCount == null) {
                tokenCount = count;
            } else {
                tokenCount += count;
            }

            return tokenCount;
        });
    }

    private void addTokenToPostingList(int documentId, Object token) {
        invertedIndex.computeIfAbsent(token, k -> new PlainJavaPostingList()).add(documentId);
    }

    /**
     * Search documents containing this given token
     *
     * @param token token obtained by document field analysis
     * @return the posting list or null if not found
     */
    public PlainJavaPostingList.PostingIterator searchDocument(Object token) {
        // TODO-06-d Have a look at this, here we call the invertedIndex (You shall add a breakpoint here during Debug session)
        PlainJavaPostingList postingList = invertedIndex.get(token);
        if (postingList != null) {
            return (PlainJavaPostingList.PostingIterator) postingList.listIterator();
        }
        return null;
    }

    public Object getTokenForDocument(int documentId) {
        if (isCommitted) {
            return columnarStorage[documentId];
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Finalize posting lists and token counts per document.
     */
    public void commit() {
        invertedIndex.forEach((key, value) -> value.commit());

        // create final token count array
        if (!temporaryTokenCountPerDocument.isEmpty()) {
            Integer biggestDocumentId = temporaryTokenCountPerDocument.lastKey();

            if (biggestDocumentId != null) {
                tokenCountPerDocument = new int[biggestDocumentId + 1];
                temporaryTokenCountPerDocument.entrySet().forEach(e -> tokenCountPerDocument[e.getKey()] = e.getValue());

                // free temporary storage
                temporaryTokenCountPerDocument = null;
            } else {
                tokenCountPerDocument = new int[0];
            }
        }
        isCommitted = true;
    }

    /**
     * Initialize columnar storage array.
     *
     * @param size size of the array
     */
    public void setColumnarStorageSize(int size) {
        if (withColumnarStorage && columnarStorage == null) {
            columnarStorage = new Object[size];
        }
    }
}
