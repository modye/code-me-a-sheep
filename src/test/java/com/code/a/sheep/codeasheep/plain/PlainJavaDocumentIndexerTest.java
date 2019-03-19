package com.code.a.sheep.codeasheep.plain;

import com.code.a.sheep.codeasheep.domain.Document;
import com.code.a.sheep.codeasheep.reader.LittlePrinceReader;
import lombok.SneakyThrows;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("plain-java")
public class PlainJavaDocumentIndexerTest {

    @Autowired
    private LittlePrinceReader littlePrinceReader;

    @Autowired
    private PlainJavaDocumentIndexer plainJavaDocumentIndexer;

    @Rule
    public final OutputCapture outputCapture = new OutputCapture();

    /**
     * Verifies 761 are bufferized
     * TODO-01-b Run this test, it should fail
     * TODO-01-d Run this test, it should pass
     */
    @Test
    public void indexDocumentsAndCheckStoreStatus() {
        // Given
        List<Document> documents = littlePrinceReader.read();

        // When
        plainJavaDocumentIndexer.indexDocuments(documents);

        // Then
        assertThat(ShamefulUtils.getInnerDocumentStore(plainJavaDocumentIndexer).getBufferedDocuments().size()).isEqualTo(761);

    }

    /**
     * Verifies 761 documents are indexed and are properly formed.
     * TODO-02-a Review this test, run it, it should fail. Why ?
     *
     * <p>
     * TODO-03-f Run this test, it should pass now :)
     */
    @Test
    public void indexDocumentsAndCheckDocumentAreComplete() {
        // Given
        List<Document> documents = littlePrinceReader.read();

        // When
        plainJavaDocumentIndexer.indexDocuments(documents);
        // TODO-02-b Look at this instruction, why do we need to call this method ?
        plainJavaDocumentIndexer.commit();

        // Then
        outputCapture.expect(containsString("Successfully indexed 761 documents"));
        Document firstDocumentMatchingMouton = getFirstDocumentMatchingMouton();

        // Verifies the field returned by our index are correct
        assertThat(firstDocumentMatchingMouton.get("text")).isEqualTo("- J'ai ton mouton. Et j'ai la caisse pour le mouton. Et j'ai la muselière...");
        assertThat(firstDocumentMatchingMouton.get("chapter")).isEqualTo("Chapitre 26");
        assertThat(firstDocumentMatchingMouton.get("isDialog")).isEqualTo(true);
        assertThat(firstDocumentMatchingMouton.get("isQuestion")).isNull();
    }

    @SneakyThrows
    private Document getFirstDocumentMatchingMouton() {
        try {
            return new ArrayList<>((plainJavaDocumentIndexer.getIndex()).search("mouton caisse", List.of(), 10).getHits()).get(0).getDocument();
        } catch (IndexOutOfBoundsException ex) {
            throw new IllegalStateException("Mmmmmh, seems like your documents are not properly indexed. Go on with the TODOs :p");
        }
    }


}
