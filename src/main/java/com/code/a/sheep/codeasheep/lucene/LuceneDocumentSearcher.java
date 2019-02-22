package com.code.a.sheep.codeasheep.lucene;

import com.code.a.sheep.codeasheep.DocumentSearcher;
import com.code.a.sheep.codeasheep.domain.Hit;
import com.code.a.sheep.codeasheep.domain.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
@DependsOn("littlePrinceReader")
public class LuceneDocumentSearcher implements DocumentSearcher {
  private final CustomAnalyzer customAnalyzer;
  private final IndexSearcher indexSearcher;

  public LuceneDocumentSearcher(CustomAnalyzer customAnalyzer, LuceneDocumentIndexer luceneDocumentIndexer) throws IOException {
    this.customAnalyzer = customAnalyzer;
    this.indexSearcher = new IndexSearcher(DirectoryReader.open(luceneDocumentIndexer.getIndexWriter()));
  }

  // TODO this function should return a Result object
  public SearchResult searchDocuments(String query) {

    try {
      Query luceneQuery = parseQuery(query, customAnalyzer);

      // TODO n=10 should be a parameter
      TopDocs topDocs = indexSearcher.search(luceneQuery, 10);
      return new SearchResult(
              topDocs.totalHits,
              Stream.of(topDocs.scoreDocs).map(this::docToHit).collect(Collectors.toList())
      );
    } catch (QueryNodeException | IOException e) {
      throw new RuntimeException("Oooops, something went bad when searching documents :/.");
    }
  }

  private Query parseQuery(String query, Analyzer customAnalyzer) throws QueryNodeException {
    StandardQueryParser standardQueryParser = new StandardQueryParser(customAnalyzer);

    // TODO default field should be a constant/parameter
    return standardQueryParser.parse(query, "text");
  }

  /**
   * Transform an infrastructure {@link ScoreDoc} object into a {@link Hit}.
   *
   * @param scoreDoc
   * @return
   */
  private Hit docToHit(ScoreDoc scoreDoc) {

    try {
      return new Hit(
              scoreDoc.score,
              indexSearcher.doc(scoreDoc.doc)
                      .getFields()
                      .stream()
                      .collect(Collectors.toMap(f -> f.name(), f -> f.stringValue()))
      );
    } catch (IOException e) {
      throw new RuntimeException("Oooops, something went bad when retrieving document " + scoreDoc.doc);
    }
  }
}
