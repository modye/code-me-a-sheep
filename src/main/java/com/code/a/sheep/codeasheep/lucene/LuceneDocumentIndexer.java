package com.code.a.sheep.codeasheep.lucene;

import com.code.a.sheep.codeasheep.domain.Document;
import com.code.a.sheep.codeasheep.interfaces.DocumentIndexer;
import com.code.a.sheep.codeasheep.lucene.schema.LuceneSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexWriter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Component used to index documents in Lucence
 */
@Component
@Profile("lucene")
@Slf4j
public class LuceneDocumentIndexer implements DocumentIndexer {
    private final IndexWriter indexWriter;
    private final LuceneSchema luceneSchema;

    public LuceneDocumentIndexer(IndexWriter indexWriter, LuceneSchema luceneSchema) {
        this.indexWriter = indexWriter;
        this.luceneSchema = luceneSchema;
    }

    @Override
    public void indexDocuments(@NotNull List<Document> documents) {
        try {
            List<org.apache.lucene.document.Document> luceneDocuments = mapToLuceneDocuments(documents);
            indexWriter.addDocuments(luceneDocuments);
            LOGGER.info("Successfully indexed {} documents", luceneDocuments.size());
        } catch (IOException ex) {
            throw new RuntimeException("Oooops, something went bad when indexing the documents", ex);
        }
    }

    @Override
    public void commit() {
        // commit to make the document searchable (in real life this should be done in an asynchronous way)
        try {
            indexWriter.commit();
        } catch (IOException ex) {
            throw new RuntimeException("Oooops, something went bad when flushing the documents", ex);
        }
    }

    /**
     * Map the documents into lucene {@link org.apache.lucene.document.Document}.
     */
    private List<org.apache.lucene.document.Document> mapToLuceneDocuments(List<com.code.a.sheep.codeasheep.domain.Document> documents) {
        return documents.stream().map(this::mapToLuceneDocument).collect(Collectors.toList());
    }

    /**
     * Transform the map of key/value into a lucene {@link org.apache.lucene.document.Document}
     * Key is the name of the field. Value, it's value.
     *
     * @param document map of key/value
     */
    private org.apache.lucene.document.Document mapToLuceneDocument(com.code.a.sheep.codeasheep.domain.Document document) {
        org.apache.lucene.document.Document luceneDocument = new org.apache.lucene.document.Document();
        document.entrySet().stream()
                .map(luceneSchema::generateLuceneFields)
                .flatMap(Collection::stream)
                .forEach(luceneDocument::add);

        return luceneDocument;
    }

    IndexWriter getIndexWriter() {
        return indexWriter;
    }
}

