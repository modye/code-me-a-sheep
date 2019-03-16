package com.code.a.sheep.codeasheep.plain.index;

import com.code.a.sheep.codeasheep.domain.Facet;
import com.code.a.sheep.codeasheep.domain.FacetValue;
import com.code.a.sheep.codeasheep.domain.Hit;
import com.code.a.sheep.codeasheep.domain.SearchResult;
import com.code.a.sheep.codeasheep.plain.schema.PlainJavaField;
import com.code.a.sheep.codeasheep.plain.schema.PlainJavaSchema;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A simple implementation of result collecting with scoring
 */
public class PlainJavaResultCollector {

    private final PlainJavaDocumentStore documentStore;
    private final PlainJavaSchema schema;

    public PlainJavaResultCollector(PlainJavaDocumentStore documentStore, PlainJavaSchema schema) {
        this.documentStore = documentStore;
        this.schema = schema;
    }

    /**
     * Collect results and compute score with a really simple function based on document/token frequencies.
     * Currently we just implement a disjunction (OR).
     *
     * @param builder      search builder
     * @param postingLists posting lists to be used
     * @return collected documents ids
     */
    public List<Integer> collectDocsAndComputeScores(SearchResult.SearchResultBuilder builder,
                                                     List<PlainJavaPostingList.PostingIterator> postingLists,
                                                     int size) {
        List<Hit> hits = new ArrayList<>();
        List<Integer> collectedDocumentIds = new ArrayList<>();

        /** Posting list are sorted and implements {@link Comparable} */
        PriorityQueue<PlainJavaPostingList.PostingIterator> mergingQueue = new PriorityQueue<>(postingLists);

        Integer currentId = -1;
        double score = 0;

        while (!mergingQueue.isEmpty()) {
            PlainJavaPostingList.PostingIterator postingList = mergingQueue.poll();
            if (postingList != null) {
                Integer id = !postingList.hasNext() ? null : postingList.checkNext();
                if (id == null || !id.equals(currentId)) {
                    if (currentId != -1) {
                        // new document id found, we finalize last one
                        hits.add(Hit.builder()
                                .score(score)
                                .document(documentStore.getDocument(currentId))
                                .build());
                    }
                    // we start scoring the new one
                    if (id != null) {
                        currentId = id;
                    }
                    score = 0;
                }
                if (id != null) {
                    collectedDocumentIds.add(id);
                    long termFrequency = getTermFrequencyInNextDocument(postingList);

                    // same document, we update the score
                    score += computeScore(postingList, currentId, termFrequency);

                    // reinsert posting list in priority queue
                    mergingQueue.add(postingList);
                }
            }
        }

        // Sort result
        hits.sort(Comparator.reverseOrder());
        builder.hits(hits.subList(0, Math.min(size, hits.size())));
        builder.nbHits(hits.size());

        return collectedDocumentIds;
    }

    /**
     * Collect facets with columnar storages of each fields.
     *
     * @param documentIds documents ids used to collect document values
     * @param facetFields fields use d for faceting
     * @return the facet list
     */
    //TODO-08-c Review this facet collect method
    List<Facet> collectFacets(List<Integer> documentIds,
                              List<String> facetFields) {
        List<Facet> facets = new ArrayList<>();

        facetFields.forEach(field -> {
            PlainJavaField plainJavaField = schema.get(field);
            if (plainJavaField == null) {
                throw new IllegalArgumentException("Unknown field '" + field + "'");
            }

            // Compute facets for this field
            Map<String, Integer> valueMap = new HashMap<>();

            documentIds.forEach(documentId -> {
                Object tokenForDocument = plainJavaField.getTokenForDocument(documentId);
                if (tokenForDocument != null) {
                    String tokenForDocumentAsString = tokenForDocument.toString();
                    valueMap.compute(tokenForDocumentAsString, (token, count) -> {
                        if (count == null) {
                            count = 0;
                        }
                        return ++count;
                    });
                }
            });

            // Add facet result

            //TODO-08-e Add a breakpoint here and see what's happening here
            List<FacetValue> facetValues = valueMap.entrySet().stream()
                    .map(e -> FacetValue.builder()
                            .count(e.getValue())
                            .key(e.getKey())
                            .build())
                    // Sort facets by count
                    .sorted(Comparator.comparingInt(FacetValue::getCount).reversed())
                    .collect(Collectors.toList());

            facets.add(Facet.builder()
                    .field(field)
                    .values(facetValues)
                    .build());
        });

        return facets;
    }

    /**
     * Count term frequency for a term in the current document.
     *
     * @param postingList the posting list to be used
     * @return the frequency
     */
    private long getTermFrequencyInNextDocument(PlainJavaPostingList.PostingIterator postingList) {
        int documentId = postingList.checkNext();
        long frequency = 0;
        do {
            frequency++;
            postingList.next();
        } while (postingList.hasNext() && postingList.checkNext() == documentId);

        return frequency;
    }

    /**
     * This method is used to compute the score
     *
     * @param postingList
     * @param documentId
     * @param termFrequency
     * @return
     */
    private double computeScore(PlainJavaPostingList.PostingIterator postingList, int documentId, long termFrequency) {
        //TODO-07-b Replace this 1 by a call to our tfIdf method
        return 1d;
    }

    /**
     * Our implementation of TF/IDF formula
     */
    private double tfIdf(PlainJavaPostingList.PostingIterator postingList, int documentId, long termFrequency) {
        return (tf(termFrequency) * idf(postingList)) / fieldLength(documentId, postingList.getSearchedField());
    }

    /**
     * Term frequency factor
     */
    private double tf(long termFrequency) {
        return Math.sqrt(termFrequency);
    }

    /**
     * Inverted document frequency factor
     */
    private double idf(PlainJavaPostingList.PostingIterator postingList) {
        // Usual idf function based on the Lucene implementation
        return 1 + Math.log((double) (documentStore.size() + 1) / ((double) postingList.getMaxSize() + 1));
    }

    /**
     * Field length is based on token count in the given field for the given document id.
     */
    private double fieldLength(int documentId, String field) {
        return Math.log1p(schema.get(field).getFieldIndex().getTokenCountForDocument(documentId));
    }
}
