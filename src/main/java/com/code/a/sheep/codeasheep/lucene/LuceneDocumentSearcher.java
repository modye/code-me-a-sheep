package com.code.a.sheep.codeasheep.lucene;

import com.code.a.sheep.codeasheep.domain.Document;
import com.code.a.sheep.codeasheep.domain.Facet;
import com.code.a.sheep.codeasheep.domain.Hit;
import com.code.a.sheep.codeasheep.domain.SearchResult;
import com.code.a.sheep.codeasheep.interfaces.DocumentSearcher;
import com.code.a.sheep.codeasheep.lucene.schema.LuceneSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.code.a.sheep.codeasheep.domain.DocumentFields.TEXT;


@Component
@Profile("lucene")
@Slf4j
@DependsOn("littlePrinceReader")
public class LuceneDocumentSearcher implements DocumentSearcher {
    private final CustomAnalyzer customAnalyzer;
    private IndexSearcher indexSearcher;
    private LuceneDocumentIndexer luceneDocumentIndexer;
    private final LuceneSchema luceneSchema;

    /**
     * Initializes component.
     *
     * @param customAnalyzer
     * @param luceneDocumentIndexer
     * @param luceneSchema
     * @throws IOException
     */
    public LuceneDocumentSearcher(CustomAnalyzer customAnalyzer, LuceneDocumentIndexer luceneDocumentIndexer, LuceneSchema luceneSchema) throws IOException {
        this.customAnalyzer = customAnalyzer;
        this.indexSearcher = new IndexSearcher(DirectoryReader.open(luceneDocumentIndexer.getIndexWriter()));
        this.luceneDocumentIndexer = luceneDocumentIndexer;
        this.luceneSchema = luceneSchema;
    }

    @Override
    public SearchResult searchDocuments(String query, List<String> facetFields) {

        try {
            Query luceneQuery = parseQuery(query, customAnalyzer);
            TopDocs topDocs = indexSearcher.search(luceneQuery, 10);

            return SearchResult.builder()
                    .nbHits(topDocs.totalHits)
                    .hits(Stream.of(topDocs.scoreDocs).map(this::docToHit).collect(Collectors.toList()))
                    .facets(aggregateResultOnField(facetFields, luceneQuery))
                    .build();

        } catch (QueryNodeException | IOException e) {
            throw new RuntimeException("Oooops, something went bad when searching documents :/.");
        }
    }

    @Override
    public void refresh() {
        // Commit
        luceneDocumentIndexer.commit();
        try {
            this.indexSearcher = new IndexSearcher(DirectoryReader.open(luceneDocumentIndexer.getIndexWriter()));
        } catch (IOException e) {
            throw new RuntimeException("Oooops, something went bad when refreshing index.");
        }
    }

    /**
     * Execute aggregations on given fields
     *
     * @param facetFields fields to execute facets on
     * @param luceneQuery query to execute
     * @return the list of built {@link Facet}
     * @throws IOException
     */
    private List<Facet> aggregateResultOnField(List<String> facetFields, Query luceneQuery) throws IOException {
        if (!CollectionUtils.isEmpty(facetFields)) {
            FacetCollector collector = new FacetCollector(facetFields, luceneSchema);
            // TODO create collector
            indexSearcher.search(luceneQuery, collector);

            return collector.getFacets();
        }
        return new ArrayList<>();
    }

    /**
     * Parse and build a lucene {@link Query}
     *
     * @param query          to search on
     * @param customAnalyzer analyzer to use for the query
     * @return built {@link Query}
     * @throws QueryNodeException
     */
    private Query parseQuery(String query, Analyzer customAnalyzer) throws QueryNodeException {
        return new StandardQueryParser(customAnalyzer).parse(query, TEXT.getName());
    }

    /**
     * Transform an infrastructure {@link ScoreDoc} object into a {@link Hit}.
     *
     * @param scoreDoc
     * @return
     */
    private Hit docToHit(ScoreDoc scoreDoc) {

        try {
            Document document = new Document(indexSearcher.doc(scoreDoc.doc)
                    .getFields()
                    .stream()
                    .collect(Collectors.toMap(IndexableField::name, f -> luceneSchema.get(f.name()).getValue(f))));

            return Hit.builder()
                    .score(scoreDoc.score)
                    .document(document)
                    .build();

        } catch (IOException e) {
            throw new RuntimeException("Oooops, something went bad when retrieving document " + scoreDoc.doc);
        }
    }
}
