package com.code.a.sheep.codeasheep.lucene;

import com.code.a.sheep.codeasheep.DocumentSearcher;
import com.code.a.sheep.codeasheep.domain.Facet;
import com.code.a.sheep.codeasheep.domain.Hit;
import com.code.a.sheep.codeasheep.domain.SearchResult;
import com.code.a.sheep.codeasheep.lucene.schema.LuceneSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.*;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
@Slf4j
@DependsOn("littlePrinceReader")
public class LuceneDocumentSearcher implements DocumentSearcher {
  private final CustomAnalyzer customAnalyzer;
  private final IndexSearcher indexSearcher;
  private final LuceneSchema luceneSchema;

  public LuceneDocumentSearcher(CustomAnalyzer customAnalyzer, LuceneDocumentIndexer luceneDocumentIndexer, LuceneSchema luceneSchema) throws IOException {
    this.customAnalyzer = customAnalyzer;
    this.indexSearcher = new IndexSearcher(DirectoryReader.open(luceneDocumentIndexer.getIndexWriter()));
    this.luceneSchema = luceneSchema;
  }

  // TODO this function should return a Result object
  public SearchResult searchDocuments(String query, List<String> facetFields) {

    try {
      Query luceneQuery = parseQuery(query, customAnalyzer);

      // TODO n=10 should be a parameter
      TopDocs topDocs = indexSearcher.search(luceneQuery, 10);

      List<Facet> facets;

      if (facetFields != null && !facetFields.isEmpty()) {
        facets = aggregateResultOnField(facetFields, luceneQuery);
      } else {
        facets = Collections.emptyList();
      }

      return SearchResult.builder()
              .nbHits(topDocs.totalHits)
              .hits(Stream.of(topDocs.scoreDocs).map(this::docToHit).collect(Collectors.toList()))
              .facets(facets)
              .build();

    } catch (QueryNodeException | IOException e) {
      throw new RuntimeException("Oooops, something went bad when searching documents :/.");
    }
  }


  private List<Facet> aggregateResultOnField(List<String> facetFields, Query luceneQuery) throws IOException {
    FacetCollector collector = new FacetCollector(facetFields, luceneSchema);
    indexSearcher.search(luceneQuery, collector);

    return collector.getFacets();
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
      Map<String, Object> document = indexSearcher.doc(scoreDoc.doc)
              .getFields()
              .stream()
              .collect(Collectors.toMap(f -> f.name(), f -> luceneSchema.get(f.name()).getValue(f)));
      return new Hit(scoreDoc.score, document);

    } catch (IOException e) {
      throw new RuntimeException("Oooops, something went bad when retrieving document " + scoreDoc.doc);
    }
  }

}
