package com.code.a.sheep.codeasheep.plain;

import com.code.a.sheep.codeasheep.domain.Document;
import com.code.a.sheep.codeasheep.interfaces.DocumentIndexer;
import com.code.a.sheep.codeasheep.plain.index.PlainJavaIndex;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

@Component
// TODO-01-a Look at this @Profile annotation, we will use Spring to switch between Lucene and Plain Java implementation
@Profile("plain-java")
@Slf4j
@Data
@RequiredArgsConstructor
public class PlainJavaDocumentIndexer implements DocumentIndexer {
    private final PlainJavaIndex index;

    @Override
    public void indexDocuments(@NotNull List<Document> documents) {

        // TODO-01-c Add documents into index
        index.addDocuments(documents);

        LOGGER.info("Successfully indexed {} documents", documents.size());
    }

    @Override
    public void commit() {
        // TODO-02-c Call the index to commit
        index.commit();
    }
}