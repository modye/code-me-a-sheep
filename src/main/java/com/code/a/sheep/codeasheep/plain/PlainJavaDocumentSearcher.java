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
        index = documentIndexer.getIndex();
    }

    @Override
    public SearchResult searchDocuments(@NotNull String query, List<String> facetFields) {

        // TODO-06-b Remove the exception, call the index to perform a search operations, returns only the 10 most relevant documents
        return index.search(query, facetFields, 10);
    }

    @Override
    public void initializeIndexSearcher() {
        // Nothing to do here :)
    }
}
