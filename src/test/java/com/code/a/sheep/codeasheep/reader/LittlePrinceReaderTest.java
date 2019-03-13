package com.code.a.sheep.codeasheep.reader;


import com.code.a.sheep.codeasheep.domain.Document;
import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the {@link LittlePrinceReader} class.
 */
public class LittlePrinceReaderTest {

    /**
     * Verifies we read 6 lines from the extract
     * TODO-01-a Run this test, it should fail
     * TODO-02-b Run this test, it should pass
     */
    @SneakyThrows
    @Test
    public void read() {

        // Given
        LittlePrinceReader reader = new LittlePrinceReader(new ClassPathResource("le-petit-prince-extract.txt"));

        // When
        List<Document> documents = reader.read();

        // Then
        assertThat(documents).hasSize(6);
        assertThat(documents).containsAll(expectedDocuments());
    }

    /**
     * TODO-01-b Review the document we are expecting
     *
     * @return
     */
    private List<Document> expectedDocuments() {

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
                                "text", "Un mouton sauvage apparait !"
                        )
                ), new Document(
                        Map.of(
                                "chapter", "Chapitre 1",
                                "isDialog", true,
                                "isQuestion", true,
                                "text", "- Voulez vous lancer une MoutonBall de type classique ?"
                        )
                ), new Document(
                        Map.of(
                                "chapter", "Chapitre 1",
                                "isDialog", true,
                                "text", "- Oui !"
                        )
                ), new Document(
                        Map.of(
                                "chapter", "Chapitre 2",
                                "text", "Chapitre 2"
                        )
                ), new Document(
                        Map.of(
                                "chapter", "Chapitre 2",
                                "text", "Vous avez attrapé un Moucool !"
                        )
                )
        );
    }
}
