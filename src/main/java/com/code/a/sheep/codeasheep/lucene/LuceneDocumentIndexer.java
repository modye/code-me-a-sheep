package com.code.a.sheep.codeasheep.lucene;

import com.code.a.sheep.codeasheep.DocumentIndexer;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class LuceneDocumentIndexer implements DocumentIndexer {
  private IndexWriter indexWriter;

  public LuceneDocumentIndexer(CustomAnalyzer customAnalyzer) throws IOException {
    this.indexWriter = new IndexWriter(new RAMDirectory(), new IndexWriterConfig(customAnalyzer));
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

    String text = (String)document.get("text");
    String chapter = (String)document.get("chapter");
    Boolean isQuestion = (Boolean)document.get("isQuestion");
    Boolean isDialog = (Boolean)document.get("isDialog");
    if (text != null) {
      luceneDocument.add(new TextField("text", text, Field.Store.YES));
    }

    // raw docvalue fields are added for faceting/aggregations
    if (chapter != null) {
      luceneDocument.add(new TextField("chapter", chapter, Field.Store.YES));
      luceneDocument.add(new SortedDocValuesField("chapter.raw", new BytesRef(chapter)));
    }

    if (isDialog != null) {
      luceneDocument.add(new TextField("isDialog", "true", Field.Store.YES));
      luceneDocument.add(new NumericDocValuesField("isDialog.raw", 1));
    }

    if (isQuestion != null) {
      luceneDocument.add(new TextField("isQuestion", "true", Field.Store.YES));
      luceneDocument.add(new NumericDocValuesField("isQuestion.raw", 1));
    }

    return luceneDocument;
  }
}
