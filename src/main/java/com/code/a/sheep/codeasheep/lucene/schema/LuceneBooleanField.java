package com.code.a.sheep.codeasheep.lucene.schema;

import lombok.Builder;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.search.DocIdSetIterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LuceneBooleanField extends LuceneField<Boolean> {

  private static final String FAST_SUFFIX = ".fast";

  @Builder
  public LuceneBooleanField(String name, boolean isStored, boolean withRawField) {
    super(name, isStored, withRawField);
  }

  public List<Field> generateLuceneFields(Map.Entry<String, Boolean> field) {
    List<Field> luceneFields = new ArrayList<>();
    int value = field.getValue() == true ? 1 : 0;

    luceneFields.add(new IntPoint(field.getKey() + FAST_SUFFIX, value));

    if (isWithRawField()) {
      luceneFields.add(new NumericDocValuesField(getRawFieldName(field.getKey()), value));
    }

    if (isStored()) {
      luceneFields.add(new StoredField(field.getKey(), value));
    }

    return luceneFields;
  }

  @Override
  public DocIdSetIterator getDocIdSetIterator(LeafReader reader) throws IOException {
    return reader.getNumericDocValues(getRawFieldName(name));
  }

  @Override
  public Boolean getDocValue(DocIdSetIterator docIdSetIterator) throws IOException {
    return ((NumericDocValues) docIdSetIterator).longValue() == 1;
  }

  @Override
  public Boolean getValue(IndexableField f) {
    return f.numericValue().intValue() == 1;
  }
}
