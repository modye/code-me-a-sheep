package com.code.a.sheep.codeasheep.lucene;

import com.code.a.sheep.codeasheep.DocumentSearcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class LuceneDocumentSearcher implements DocumentSearcher {
  private final CustomAnalyzer customAnalyzer;
  private final IndexSearcher indexSearcher;

  public LuceneDocumentSearcher(CustomAnalyzer customAnalyzer, LuceneDocumentIndexer luceneDocumentIndexer) throws IOException {
    this.customAnalyzer = customAnalyzer;
    this.indexSearcher = new IndexSearcher(DirectoryReader.open(luceneDocumentIndexer.getIndexWriter()));
  }

  // TODO this function should return a Result object
  public TopDocs searchDocuments(String query) throws QueryNodeException, IOException {
    Query luceneQuery = parseQuery(query, customAnalyzer);

    // TODO n=10 should be a parameter
    TopDocs topDocs = indexSearcher.search(luceneQuery, 10);

    // TODO iterate on results and create the result object
    return topDocs;
  }

  private Query parseQuery(String query, Analyzer customAnalyzer) throws QueryNodeException {
    StandardQueryParser standardQueryParser = new StandardQueryParser(customAnalyzer);

    // TODO default field should be a constant/parameter
    return standardQueryParser.parse(query, "text");
  }
}
