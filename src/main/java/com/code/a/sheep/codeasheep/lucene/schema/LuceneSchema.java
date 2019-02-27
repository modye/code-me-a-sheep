package com.code.a.sheep.codeasheep.lucene.schema;

import org.apache.lucene.document.Field;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LuceneSchema extends HashMap<String, LuceneField> {
  public LuceneSchema addField(LuceneField field) {
    this.put(field.getName(), field);

    return this;
  }

  public List<Field> generateLuceneFields(Map.Entry<String, Object> field) {
    LuceneField luceneField = get(field.getKey());

    if (luceneField == null) {
      throw new IllegalStateException("Field '" + field.getKey() + "' not found");
    }

    return luceneField.generateLuceneFields(field);
  }
}
