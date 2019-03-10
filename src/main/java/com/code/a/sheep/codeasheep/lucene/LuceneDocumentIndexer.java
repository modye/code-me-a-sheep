package com.code.a.sheep.codeasheep.lucene;

import com.code.a.sheep.codeasheep.interfaces.DocumentIndexer;
import com.code.a.sheep.codeasheep.lucene.schema.LuceneSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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

    public LuceneDocumentIndexer(CustomAnalyzer customAnalyzer, LuceneSchema luceneSchema) throws IOException {
        this.indexWriter = new IndexWriter(new RAMDirectory(), new IndexWriterConfig(customAnalyzer));
        this.luceneSchema = luceneSchema;
    }

    @Override
    public void indexDocuments(@NotNull List<Map<String, Object>> documents) {
        try {
            List<Document> luceneDocuments = mapToLuceneDocuments(documents);
            // Index the document in Lucene
            indexWriter.addDocuments(luceneDocuments);
            // flush to make the document searchable (in real life this should be done in an asynchronous way)
            indexWriter.flush();
        } catch (IOException ex) {
            throw new RuntimeException("Oooops, something went bad when indexing the documents", ex);
        }
    }

    /**
     * Map the documents into lucene {@link Document}.
     */
    private List<Document> mapToLuceneDocuments(List<Map<String, Object>> documents) {
        return documents.stream().map(this::mapToLuceneDocument).collect(Collectors.toList());
    }

    /**
     * Transform the map of key/value into a lucene {@link Document}
     * Key is the name of the field. Value, it's value.
     *
     * @param document map of key/value
     */
    private Document mapToLuceneDocument(Map<String, Object> document) {
        Document luceneDocument = new Document();
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
