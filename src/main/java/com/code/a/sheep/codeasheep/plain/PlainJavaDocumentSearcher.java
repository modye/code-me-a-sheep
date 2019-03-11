package com.code.a.sheep.codeasheep.plain;

import com.code.a.sheep.codeasheep.domain.SearchResult;
import com.code.a.sheep.codeasheep.interfaces.DocumentSearcher;
import com.code.a.sheep.codeasheep.plain.index.PlainJavaIndex;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

@Component
@Profile("plain-java")
public class PlainJavaDocumentSearcher implements DocumentSearcher {
    private final PlainJavaIndex index;

    public PlainJavaDocumentSearcher(PlainJavaDocumentIndexer documentIndexer) {
        this.index = documentIndexer.getIndex();
    }

    @Override
    public SearchResult searchDocuments(@NotNull String query, List<String> facetFields) {
        // TODO should be a scored result, not a simple posting list
        // TODO size should be a parameter
        return index.search(query, 10);
    }

    @Override
    public void refresh() {
        index.commit();
    }
}
