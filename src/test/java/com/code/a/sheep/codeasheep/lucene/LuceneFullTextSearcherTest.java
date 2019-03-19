package com.code.a.sheep.codeasheep.lucene;

import com.code.a.sheep.codeasheep.domain.Document;
import com.code.a.sheep.codeasheep.domain.SearchResult;
import com.code.a.sheep.codeasheep.reader.LittlePrinceReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("lucene")
public class LuceneFullTextSearcherTest {

    @Autowired
    private LittlePrinceReader littlePrinceReader;

    @Autowired
    private LuceneDocumentIndexer luceneDocumentIndexer;

    @Autowired
    private LuceneDocumentSearcher luceneDocumentSearcher;

    /**
     * Executes a full text search on the word 'moUtôn' and verifies it's result
     * <p>
     */
    @Test
    public void searchDocuments_fullText() {

        // Given
        List<Document> documents = littlePrinceReader.read();
        luceneDocumentIndexer.indexDocuments(documents);
        luceneDocumentIndexer.commit();
        luceneDocumentSearcher.initializeIndexSearcher();

        // When
        SearchResult searchResult = luceneDocumentSearcher.searchDocuments("moUtôn", List.of());

        // Then
        assertThat(searchResult).isNotNull();
        assertThat(searchResult.getNbHits()).isEqualTo(29);
        assertThat(searchResult.getHits().size()).isEqualTo(10);
        assertThat(searchResult.getFacets().size()).isEqualTo(0);
    }
}
