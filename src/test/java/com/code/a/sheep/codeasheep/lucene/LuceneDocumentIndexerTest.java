package com.code.a.sheep.codeasheep.lucene;

import com.code.a.sheep.codeasheep.domain.Document;
import com.code.a.sheep.codeasheep.reader.LittlePrinceReader;
import lombok.SneakyThrows;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
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
import static org.hamcrest.Matchers.containsString;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("lucene")
public class LuceneDocumentIndexerTest {

    @Autowired
    private LittlePrinceReader littlePrinceReader;

    @Autowired
    private LuceneDocumentIndexer luceneDocumentIndexer;

    @Rule
    public final OutputCapture outputCapture = new OutputCapture();

    /**
     * TODO-04-a Execute this test it should pass
     * TODO-04-c Execute this test, it should fail. When the test is green, remove it
     */
    @Test(expected = RuntimeException.class)
    public void notYetImplemented() {
        // Given
        List<Document> documents = littlePrinceReader.read();

        // When
        luceneDocumentIndexer.indexDocuments(documents);
    }

    /**
     * Verifies 761 documents are indexed and are properly formed.
     * TODO-05-a Run this test it should fail
     * TODO-05-d Run this test again, it should pass
     * You can see Lucene retrieves boolean field as "O/1", that's why a mapping phase is needed : {@link com.code.a.sheep.codeasheep.lucene.schema.LuceneBooleanField#getValue(IndexableField)}
     */
    @Test
    public void indexDocumentsAndCheckLuceneDocumentAreComplete() {
        // Given
        List<Document> documents = littlePrinceReader.read();

        // When
        luceneDocumentIndexer.indexDocuments(documents);

        // Then
        outputCapture.expect(containsString("Successfully indexed 761 documents"));
        org.apache.lucene.document.Document firstDocumentMatchingMouton = getFirstDocumentMatchingMouton();

        // Verifies the field returned by Lucene are correct
        assertThat(firstDocumentMatchingMouton.get("text")).isEqualTo("- J'ai ton mouton. Et j'ai la caisse pour le mouton. Et j'ai la muselière...");
        assertThat(firstDocumentMatchingMouton.get("chapter")).isEqualTo("Chapitre 26");
        assertThat(firstDocumentMatchingMouton.get("isDialog")).isEqualTo("1");
        assertThat(firstDocumentMatchingMouton.get("isQuestion")).isNull();
    }

    @SneakyThrows
    private org.apache.lucene.document.Document getFirstDocumentMatchingMouton() {
        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(luceneDocumentIndexer.getIndexWriter()));
        TopDocs search = indexSearcher.search(new StandardQueryParser().parse("mouton", "text"), 1);
        return indexSearcher.doc(search.scoreDocs[0].doc);
    }
}
