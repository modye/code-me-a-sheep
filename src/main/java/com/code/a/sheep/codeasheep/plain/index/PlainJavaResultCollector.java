package com.code.a.sheep.codeasheep.plain.index;

import com.code.a.sheep.codeasheep.domain.Document;
import com.code.a.sheep.codeasheep.domain.Hit;
import com.code.a.sheep.codeasheep.domain.SearchResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * A simple implementation of result collecting with scoring
 */
public class PlainJavaResultCollector {

    private final PlainJavaDocumentStore documentStore;

    public PlainJavaResultCollector(PlainJavaDocumentStore documentStore) {
        this.documentStore = documentStore;
    }

    /**
     * Collect results and compute score with a really simple function based on document/token frequencies.
     * Currently we just implement a disjunction (OR).
     *
     * @param postingLists posting lists to be used
     * @return a search result
     */
    public SearchResult collectAndComputeScore(List<PlainJavaPostingList> postingLists, int size) {
        SearchResult.SearchResultBuilder builder = SearchResult.builder();
        List<Hit> hits = new ArrayList<>();

        /** Posting list are sorted and implements {@link Comparable} */
        PriorityQueue<PlainJavaPostingList> mergingQueue = new PriorityQueue<>(postingLists);

        Integer currentId = -1;
        float score = 0;

        while (!mergingQueue.isEmpty()) {
            PlainJavaPostingList postingList = mergingQueue.poll();
            if (postingList != null) {
                Integer id = postingList.pollFirst();
                if (id == null || !id.equals(currentId)) {
                    if (currentId != -1) {
                        // new document id found, we finalize last one
                        hits.add(Hit.builder()
                                .score(score)
                                .document(documentStore.getDocument(currentId))
                                .build());
                    }
                    // we start scoring the new one
                    currentId = id;
                    if (id != null) {
                        score = computeScore(postingList, documentStore.getDocument(currentId));
                    }
                } else {
                    // Same document, we update the score
                    // TODO this should be a bit more complicated (check tf/idf formulae)
                    score += computeScore(postingList, documentStore.getDocument(currentId));
                }

                // reinsert posting list in priority queue
                if (id != null) {
                    mergingQueue.add(postingList);
                }
            }
        }

        // Sort result
        hits.sort(Comparator.reverseOrder());
        builder.hits(hits.subList(0, Math.min(size, hits.size())));
        builder.nbHits(hits.size());

        return builder.build();
    }

    private float computeScore(PlainJavaPostingList postingList, Document document) {
        return idf(postingList) / fieldLength(document, postingList.getSearchedField());
    }

    private float idf(PlainJavaPostingList postingList) {
        return (float) documentStore.size() / (float) postingList.getMaxSize();
    }

    private float fieldLength(Document document, String field) {
        return document.get(field).toString().split(" ").length;
    }
}
