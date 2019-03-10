package com.code.a.sheep.codeasheep.plain;

import com.code.a.sheep.codeasheep.interfaces.DocumentIndexer;
import com.code.a.sheep.codeasheep.plain.index.PlainJavaIndex;
import com.code.a.sheep.codeasheep.plain.schema.PlainJavaSchema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Component
@Profile("plain-java")
@Slf4j
@Data
public class PlainJavaDocumentIndexer implements DocumentIndexer {
    private final PlainJavaIndex index;

    public PlainJavaDocumentIndexer(PlainJavaSchema schema) {
        this.index = new PlainJavaIndex(schema);
    }

    @Override
    public void indexDocuments(@NotNull List<Map<String, Object>> documents) {
        documents.stream().forEach(index::addDocument);
    }
}
