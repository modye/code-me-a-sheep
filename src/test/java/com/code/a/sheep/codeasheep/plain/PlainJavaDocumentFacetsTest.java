package com.code.a.sheep.codeasheep.plain;

import com.code.a.sheep.codeasheep.domain.Document;
import com.code.a.sheep.codeasheep.domain.FacetValue;
import com.code.a.sheep.codeasheep.domain.SearchResult;
import com.code.a.sheep.codeasheep.reader.LittlePrinceReader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("plain-java")
public class PlainJavaDocumentFacetsTest {

    @Autowired
    private LittlePrinceReader littlePrinceReader;

    @Autowired
    private PlainJavaDocumentIndexer plainJavaDocumentIndexer;

    @Autowired
    private PlainJavaDocumentSearcher plainJavaDocumentSearcher;

    @Rule
    public final OutputCapture outputCapture = new OutputCapture();

    /**
     * Executes a full text search on the word 'mouton' and aggregate on the field chapter
     * This is the same test as the first lab with Lucene :)
     * TODO-08-a Run this test, it will fail
     * TODO-08-d Run this test, it should pass
     * TODO-08-g Run this test, it will fail
     */
    @Test
    public void searchDocuments_facets() {

        // Given
        List<Document> documents = littlePrinceReader.read();
        plainJavaDocumentIndexer.indexDocuments(documents);
        plainJavaDocumentIndexer.commit();

        // When
        SearchResult searchResult = plainJavaDocumentSearcher.searchDocuments("mouton", List.of("chapter"));

        // Then
        assertThat(searchResult).isNotNull();
        assertThat(searchResult.getNbHits()).isEqualTo(29);
        assertThat(searchResult.getHits().size()).isEqualTo(10);

        assertThat(searchResult.getFacets()).isNotNull();
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
