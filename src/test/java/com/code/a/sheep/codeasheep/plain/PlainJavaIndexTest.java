package com.code.a.sheep.codeasheep.plain;

import com.code.a.sheep.codeasheep.domain.Document;
import com.code.a.sheep.codeasheep.plain.index.PlainJavaFieldIndex;
import com.code.a.sheep.codeasheep.plain.index.PlainJavaIndex;
import com.code.a.sheep.codeasheep.plain.index.PlainJavaPostingList;
import com.code.a.sheep.codeasheep.plain.schema.PlainJavaField;
import com.code.a.sheep.codeasheep.plain.schema.PlainJavaSchema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("plain-java")
public class PlainJavaIndexTest {

    @Autowired
    private PlainJavaIndex plainJavaIndex;

    /**
     * This method tests {@link PlainJavaIndex#commit()} behavior.
     * TODO-03-b Run this test, it should fail, why ?
     * TODO-03-e Run this test, it should pass now :)
     * When it's green, you can add @Ignore on it and go to 04-a
     * Are you confident with the token produced ?
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void commitAndCheckInvertedIndex() {

        // Given
        List<Document> documents = documents();
        plainJavaIndex.addDocuments(documents);

        // When
        plainJavaIndex.commit();

        // Then
        PlainJavaSchema schema = ShamefulUtils.getSchema(plainJavaIndex);

        PlainJavaField text = schema.get("text");
        assertThat(text.isWithColumnarStorage()).isFalse();
        Map<Object, PlainJavaPostingList> textInvertedIndex = ShamefulUtils.retrievesFieldInvertedIndex(text.getFieldIndex());

        // Verifies the inverted index for field "text" contains all tokens
        assertThat(textInvertedIndex.keySet()).contains("Chapitre", "mouton", "SAUVAGE", "vraiment", "apparait");
    }

    /**
     * As we saw in Lucene lab, text analysis is really important during indexing time to provide full text search
     * TODO-04-a Run this test it should fail, why is it failing ?
     * TODO-04-d Run this test it should pass
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void commitAndCheckInvertedIndexWithTextAnalysis() {

        // Given
        List<Document> documents = documents();
        plainJavaIndex.addDocuments(documents);

        // When
        plainJavaIndex.commit();

        // Then
        PlainJavaSchema schema = ShamefulUtils.getSchema(plainJavaIndex);

        PlainJavaField text = schema.get("text");
        assertThat(text.isWithColumnarStorage()).isFalse();
        Map<Object, PlainJavaPostingList> textInvertedIndex = ShamefulUtils.retrievesFieldInvertedIndex(text.getFieldIndex());

        assertThat(textInvertedIndex.keySet()).contains("chapitre", "mouton", "sauvage", "vraiment", "apparait");

        assertThat(textInvertedIndex.get("chapitre")).containsExactly(0);
        assertThat(textInvertedIndex.get("mouton")).containsExactly(1, 2);
        assertThat(textInvertedIndex.get("sauvage")).containsExactly(1, 2);
        assertThat(textInvertedIndex.get("vraiment")).containsExactly(2);
        assertThat(textInvertedIndex.get("apparait")).containsExactly(1);
    }

    /**
     * Verifies doc values structure are correctly formed. This columnar storage will be really useful while using sorting and facets.
     * TODO-05-a First be sure to understand what's under testing here, run this test, it should fail
     * TODO-05-c Run this test, it should pass
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void commitAndCheckDocValues() {

        // Given
        List<Document> documents = documents();
        plainJavaIndex.addDocuments(documents);

        // When
        plainJavaIndex.commit();

        // Then
        PlainJavaSchema schema = ShamefulUtils.getSchema(plainJavaIndex);

        PlainJavaFieldIndex chapter = schema.get("chapter").getFieldIndex();
        assertThat(chapter.getTokenForDocument(0)).isEqualTo("Chapitre 1");
        assertThat(chapter.getTokenForDocument(1)).isEqualTo("Chapitre 1");
        assertThat(chapter.getTokenForDocument(2)).isEqualTo("Chapitre 2");

        PlainJavaFieldIndex isDialog = schema.get("isDialog").getFieldIndex();
        assertThat(isDialog.getTokenForDocument(2)).isEqualTo("true");

        PlainJavaFieldIndex isQuestion = schema.get("isQuestion").getFieldIndex();
        assertThat(isQuestion.getTokenForDocument(2)).isEqualTo("true");

        PlainJavaField text = schema.get("text");
        assertThat(text.isWithColumnarStorage()).isFalse();
    }

    private List<Document> documents() {

        return List.of(
                new Document(
                        Map.of(
                                "chapter", "Chapitre 1",
                                "text", "Chapitre 1"
                        )
                ),
                new Document(
                        Map.of(
                                "chapter", "Chapitre 1",
                                "text", "Un mouton SAUVAGE apparait !"
                        )
                ), new Document(
                        Map.of(
                                "chapter", "Chapitre 2",
                                "isDialog", true,
                                "isQuestion", true,
                                "text", "- Ce mouton est il vraiment sauvage ?"
                        )
                )
        );

    }
}
