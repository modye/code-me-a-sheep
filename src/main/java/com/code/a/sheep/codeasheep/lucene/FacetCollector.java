package com.code.a.sheep.codeasheep.lucene;

import com.code.a.sheep.codeasheep.domain.Facet;
import com.code.a.sheep.codeasheep.domain.FacetValue;
import com.code.a.sheep.codeasheep.lucene.schema.LuceneSchema;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.SimpleCollector;
import org.apache.lucene.util.mutable.MutableValueInt;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This collector is used to retrieve all document values for faceting purposes.
 */
class FacetCollector extends SimpleCollector {
  private final Map<String, DocIdSetIterator> docIdSetIteratorMap;
  private final Map<String, Map<Object, MutableValueInt>> facets;
  private final List<String> fields;
  private final LuceneSchema schema;

  public FacetCollector(List<String> fields, LuceneSchema schema) {
    this.fields = fields;
    this.schema = schema;
    facets = new HashMap<>();
    docIdSetIteratorMap = new HashMap<>();

    // init facet map
    fields.stream().forEach(f -> facets.put(f, new HashMap<>()));
  }

  /**
   *
   * @param context new Lucene leaf reader context to be used
   * @throws IOException
   */
  @Override
  protected void doSetNextReader(LeafReaderContext context) throws IOException {
    docIdSetIteratorMap.clear();
    for (String field : fields) {
      DocIdSetIterator docIdSetIterator = schema.get(field).getDocIdSetIterator(context.reader());
      if (docIdSetIterator == null) {
        throw new IllegalArgumentException("Field '" + field + "' is not a doc valued field");
      }
      docIdSetIteratorMap.put(field, docIdSetIterator);
    }
  }

  @Override
  public void collect(int doc) throws IOException {
    for (String field : fields) {
      DocIdSetIterator docIdSetIterator = docIdSetIteratorMap.get(field);
      if (docIdSetIterator.advance(doc) == doc) {
        facets.get(field).compute(schema.get(field).getDocValue(docIdSetIterator), (k, v) -> {
          if (v == null) {
            v = new MutableValueInt();
          }
          v.value++;

          return v;
        });
      }
    }
  }

  @Override
  public boolean needsScores() {
    return false;
  }

  public List<Facet> getFacets() {
    return facets.entrySet().stream()
            .map(e -> Facet.builder()
                    .field(e.getKey())
                    .values(getFacet(e.getValue()))
                    .build())
            .collect(Collectors.toList());
  }

  public List<FacetValue> getFacet(Map<Object, MutableValueInt> facet) {
    return facet.entrySet().stream()
            .sorted(Comparator.comparingInt(o -> -o.getValue().value))
            .map(e -> FacetValue.builder().key(e.getKey()).count(e.getValue().value).build())
            .collect(Collectors.toList());
  }
}
