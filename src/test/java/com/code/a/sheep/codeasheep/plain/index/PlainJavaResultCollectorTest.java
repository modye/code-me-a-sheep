package com.code.a.sheep.codeasheep.plain.index;

import com.code.a.sheep.codeasheep.domain.Document;
import com.code.a.sheep.codeasheep.domain.Hit;
import com.code.a.sheep.codeasheep.domain.SearchResult;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PlainJavaResultCollectorTest {


    @Test
    public void collectAndComputeScore() {
        // Given
        PlainJavaResultCollector collector = new PlainJavaResultCollector(documentStore());

        PlainJavaPostingList postingList = new PlainJavaPostingList();
        postingList.add(0);
        postingList.add(1);
        postingList.add(3);
        postingList.add(5);
        postingList.setSearchedField("text");
        postingList.commit();

        // When
        SearchResult searchResult = collector.collectAndComputeScore(List.of(postingList), 3);

        // Then
        assertThat(searchResult).isNotNull();
        assertThat(searchResult.getNbHits()).isEqualTo(4L);
        ArrayList<Hit> hits = new ArrayList<>(searchResult.getHits());
        assertThat(hits.size()).isEqualTo(3);
        assertThat(hits.get(0).getDocument().get("text")).isEqualTo("Incredible mouton");
        assertThat(hits.get(1).getDocument().get("text")).isEqualTo("Not bad mouton");
        assertThat(hits.get(2).getDocument().get("text")).isEqualTo("This mouton is nice");
    }

    private PlainJavaDocumentStore documentStore() {
        PlainJavaDocumentStore plainJavaDocumentStore = new PlainJavaDocumentStore();
        List.of(
                new Document(Map.of(
                        "chapter", "Chapitre 1",
                        "text", "Not bad mouton")
                ),
                new Document(Map.of(
                        "chapter", "Chapitre 1",
                        "text", "Mouton was matched but this text is super long")
                ),
                new Document(Map.of(
                        "chapter", "Chapitre 1",
                        "text", "Definitely a chicken")
                ),
                new Document(Map.of(
                        "chapter", "Chapitre 1",
                        "text", "Incredible mouton")
                ),
                new Document(Map.of(
                        "chapter", "Chapitre 1",
                        "text", "This one is a boa")
                ),
                new Document(Map.of(
                        "chapter", "Chapitre 1",
                        "text", "This mouton is nice")
                )
        ).forEach(plainJavaDocumentStore::addDocument);

        plainJavaDocumentStore.commit();
        return plainJavaDocumentStore;
    }
}