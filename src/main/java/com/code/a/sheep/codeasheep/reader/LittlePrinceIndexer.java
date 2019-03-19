package com.code.a.sheep.codeasheep.reader;

import com.code.a.sheep.codeasheep.domain.Document;
import com.code.a.sheep.codeasheep.interfaces.DocumentIndexer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Index {@link Document} in the system.
 */
@Component
@RequiredArgsConstructor
public class LittlePrinceIndexer {
    private final DocumentIndexer documentIndexer;

    /**
     * Index and commit list of {@link Document} in the system.
     *
     * @param documents
     */
    public void indexAndCommit(List<Document> documents) {
        documentIndexer.indexDocuments(documents);
        // commit index
        documentIndexer.commit();
    }
}
