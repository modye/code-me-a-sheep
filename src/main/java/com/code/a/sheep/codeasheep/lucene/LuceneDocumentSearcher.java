package com.code.a.sheep.codeasheep.lucene;

import com.code.a.sheep.codeasheep.DocumentSearcher;
import com.code.a.sheep.codeasheep.domain.Facet;
import com.code.a.sheep.codeasheep.domain.Hit;
import com.code.a.sheep.codeasheep.domain.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.SortedDocValues;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.util.mutable.MutableValueInt;
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

      //TODO generify faceting (Should be a list of fields and should support multiple types)
      List<Facet> facets = aggregateResultOnStringField("chapter.raw", luceneQuery);

      return SearchResult.builder()
              .nbHits(topDocs.totalHits)
              .hits(Stream.of(topDocs.scoreDocs).map(this::docToHit).collect(Collectors.toList()))
              .facets(facets)
              .build();
    } catch (QueryNodeException | IOException e) {
      throw new RuntimeException("Oooops, something went bad when searching documents :/.");
    }
  }


  private List<Facet> aggregateResultOnStringField(String field, Query luceneQuery) throws IOException {
    StringFacetCollector collector = new StringFacetCollector(field);
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

  private class StringFacetCollector extends SimpleCollector {
    private SortedDocValues sortedDocValues;
    private final Map<String, MutableValueInt> facets;
    private final String field;

    public StringFacetCollector(String field) {
      this.field = field;
      facets = new HashMap<>();
    }

    protected void doSetNextReader(LeafReaderContext context) throws IOException {
      sortedDocValues = context.reader().getSortedDocValues(field);
      if (sortedDocValues == null) {
        throw new IllegalArgumentException("Field '" + field + "' is not a docvalued field");
      }
    }

    @Override
    public void collect(int doc) throws IOException {
      if (sortedDocValues.advanceExact(doc)) {
        facets.compute(sortedDocValues.binaryValue().utf8ToString(), (k, v) -> {
          if (v == null) {
            v = new MutableValueInt();
          }
          v.value++;

          return v;
        });
      }
    }

    @Override
    public boolean needsScores() {
      return false;
    }

    public List<Facet> getFacets() {
      return facets.entrySet().stream()
              .sorted(Comparator.comparingInt(o -> -o.getValue().value))
              .map(e -> Facet.builder().key(e.getKey()).count(e.getValue().value).build())
              .collect(Collectors.toList());

    }
  }
}
