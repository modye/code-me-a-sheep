package com.code.a.sheep.codeasheep.lucene;

import com.code.a.sheep.codeasheep.DocumentIndexer;
import com.code.a.sheep.codeasheep.lucene.schema.LuceneSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class LuceneDocumentIndexer implements DocumentIndexer {
  private final IndexWriter indexWriter;
  private final LuceneSchema luceneSchema;

  public LuceneDocumentIndexer(CustomAnalyzer customAnalyzer, LuceneSchema luceneSchema) throws IOException {
    this.indexWriter = new IndexWriter(new RAMDirectory(), new IndexWriterConfig(customAnalyzer));
    this.luceneSchema = luceneSchema;
  }

  public IndexWriter getIndexWriter() {
    return indexWriter;
  }

  // TODO this function should return an indexing result object ?
  public void indexDocuments(@NotNull List<Map<String, Object>> documents) throws IOException {
    List<Document> luceneDocuments = mapToLuceneDocuments(documents);
    // Index the document in Lucene
    indexWriter.addDocuments(luceneDocuments);
    // flush to make the document searchable (in real life this should be done in an asynchronous way)
    indexWriter.flush();
  }

  private List<Document> mapToLuceneDocuments(List<Map<String, Object>> documents) {
    return documents.stream().map(this::mapToLuceneDocument).collect(Collectors.toList());
  }

  private Document mapToLuceneDocument(Map<String, Object> document) {
    Document luceneDocument = new Document();
    document.entrySet().stream()
            .map(luceneSchema::generateLuceneFields)
            .flatMap(fields -> fields.stream())
            .forEach(luceneDocument::add);

    return luceneDocument;
  }
}
