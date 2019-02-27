package com.code.a.sheep.codeasheep.lucene.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.search.DocIdSetIterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Data
public abstract class LuceneField<T> {
  private static final String RAW_SUFFIX = ".raw";
  protected final String name;

  private final boolean isStored;
  private final boolean withRawField;

  public abstract List<Field> generateLuceneFields(Map.Entry<String, T> field);

  protected String getRawFieldName(String fieldName) {
    return fieldName + RAW_SUFFIX;
  }

  public abstract DocIdSetIterator getDocIdSetIterator(LeafReader reader) throws IOException;

  public abstract String getDocValueString(DocIdSetIterator docIdSetIterator) throws IOException;

  public abstract T getValue(IndexableField f);
}
