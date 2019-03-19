package com.code.a.sheep.codeasheep.plain;

import com.code.a.sheep.codeasheep.domain.Document;
import com.code.a.sheep.codeasheep.domain.Hit;
import com.code.a.sheep.codeasheep.domain.SearchResult;
import com.code.a.sheep.codeasheep.reader.LittlePrinceReader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("plain-java")
public class PlainJavaDocumentSearcherTest {

    @Autowired
    private LittlePrinceReader littlePrinceReader;

    @Autowired
    private PlainJavaDocumentIndexer plainJavaDocumentIndexer;

    @Autowired
    private PlainJavaDocumentSearcher plainJavaDocumentSearcher;

    @Rule
    public final OutputCapture outputCapture = new OutputCapture();

    /**
     * TODO-06-a Run this test, you should receive a Not yet implemented exception
     * TODO-06-e Run this test, it should pass
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void searchDocuments() {
        // Given
        List<Document> documents = littlePrinceReader.read();
        plainJavaDocumentIndexer.indexDocuments(documents);
        plainJavaDocumentIndexer.commit();

        // When
        SearchResult searchResult = plainJavaDocumentSearcher.searchDocuments("mouton caisse", List.of());
        // Then
        assertThat(searchResult.getNbHits()).isEqualTo(31);
        assertThat(new ArrayList<>(searchResult.getHits())).isNotEmpty().hasSize(10);
    }

    /**
     * This test verifies the scoring method we use is correct
     * TODO-07-a Run this test, it should fail
     * It's failing because we didn't setup our score formula
     * TODO-07-c Run this test, it should pass
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void searchDocumentsAndCheckScoring() {
        // Given
        List<Document> documents = littlePrinceReader.read();
        plainJavaDocumentIndexer.indexDocuments(documents);
        plainJavaDocumentIndexer.commit();

        // When
        SearchResult searchResult = plainJavaDocumentSearcher.searchDocuments("mouton caisse", List.of());
        // Then
        assertThat(searchResult.getNbHits()).isEqualTo(31);
        ArrayList<Hit> hits = new ArrayList<>(searchResult.getHits());
        assertThat(hits).isNotEmpty().hasSize(10);
        Document firstDocumentMatchingMouton = hits.get(0).getDocument();

        // Verifies the field returned by our index are correct
        assertThat(firstDocumentMatchingMouton.get("text")).isEqualTo("- J'ai ton mouton. Et j'ai la caisse pour le mouton. Et j'ai la muselière...");
        assertThat(firstDocumentMatchingMouton.get("chapter")).isEqualTo("Chapitre 26");
        assertThat(firstDocumentMatchingMouton.get("isDialog")).isEqualTo(true);
        assertThat(firstDocumentMatchingMouton.get("isQuestion")).isNull();

        //TODO-07-d Add an assertion on the score value
        assertThat(hits.get(0).getScore()).isEqualTo(3.902076622189789);
    }


}
