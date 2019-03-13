package com.code.a.sheep.codeasheep.lucene;

import com.code.a.sheep.codeasheep.domain.Document;
import com.code.a.sheep.codeasheep.domain.FacetValue;
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
public class LuceneFacetSearcherTest {

    @Autowired
    private LittlePrinceReader littlePrinceReader;

    @Autowired
    private LuceneDocumentIndexer luceneDocumentIndexer;

    @Autowired
    private LuceneDocumentSearcher luceneDocumentSearcher;

    /**
     * Executes a full text search on the word 'mouton' and aggregate on the field chapter
     * TODO-09-a Review this test and the method expectedFacetValues, what's the meaning of the FacetValue("Chapitre 2", 11) ?
     * TODO-09-b Execute this test, it should not pass
     * TODO-09-d Execute this test, it should pass
     */
    @Test
    public void searchDocuments_facets() {

        // Given
        List<Document> documents = littlePrinceReader.read();
        luceneDocumentIndexer.indexDocuments(documents);
        luceneDocumentIndexer.commit();
        luceneDocumentSearcher.initializeIndexSearcher();

        // When
        SearchResult searchResult = luceneDocumentSearcher.searchDocuments("mouton", List.of("chapter"));

        // Then
        assertThat(searchResult).isNotNull();
        assertThat(searchResult.getNbHits()).isEqualTo(29);
        assertThat(searchResult.getHits().size()).isEqualTo(10);

        assertThat(searchResult.getFacets().size()).isEqualTo(1);
        assertThat(searchResult.getFacets().get(0).getField()).isEqualTo("chapter");
        assertThat(searchResult.getFacets().get(0).getValues()).isEqualTo(expectedFacetValues());
    }

    private List<FacetValue> expectedFacetValues() {
        return List.of(
                new FacetValue("Chapitre 2", 11),
                new FacetValue("Chapitre 7", 6),
                new FacetValue("Chapitre 27", 5),
                new FacetValue("Chapitre 3", 4),
                new FacetValue("Chapitre 5", 1),
                new FacetValue("Chapitre 26", 1),
                new FacetValue("Chapitre 25", 1)
        );
    }
}
