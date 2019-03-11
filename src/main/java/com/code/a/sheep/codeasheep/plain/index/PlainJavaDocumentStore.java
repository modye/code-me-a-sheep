package com.code.a.sheep.codeasheep.plain.index;

import com.code.a.sheep.codeasheep.domain.Document;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple document store implementation.
 * Synchronization between document addition and flushing process should be done by the caller.
 */
public class PlainJavaDocumentStore {
    private Document[] store;
    private final AtomicInteger nextDocumentId;
    private boolean isCommitted;

    // This buffer stores documents until flush is called
    private Collection<PlainJavaDocument> temporaryDocumentBuffer;

    public PlainJavaDocumentStore() {
        temporaryDocumentBuffer = new ConcurrentLinkedDeque<>();
        nextDocumentId = new AtomicInteger();
        isCommitted = false;
    }

    public int size() {
        return isCommitted ? store.length : 0;
    }

    /**
     * Add a new document in the store and returns the internal document id.
     *
     * @param document the docment to be added
     * @return the internal document id
     */
    public int addDocument(Document document) {
        int id = nextDocumentId.getAndIncrement();
        PlainJavaDocument internalDocument = new PlainJavaDocument(id);
        internalDocument.putAll(document);
        temporaryDocumentBuffer.add(internalDocument);

        return id;
    }

    public Document getDocument(int id) {
        if (isCommitted) {
            return store[id];
        } else {
            return null;
        }
    }

    /**
     * Optimize document storage.
     * In this example, we just transform the buffer into a document array.
     * This is just for educational purpose.
     */
    public void commit() {
        store = new Document[temporaryDocumentBuffer.size()];
        // documents could be shuffled in the buffer
        temporaryDocumentBuffer.forEach(document -> store[document.getId()] = document);

        // free temporary buffer
        temporaryDocumentBuffer = null;

        isCommitted = true;
    }

    public Collection<PlainJavaDocument> getBufferedDocuments() {
        return isCommitted ? Collections.emptyList() : Collections.unmodifiableCollection(temporaryDocumentBuffer);
    }
}
