package com.code.a.sheep.codeasheep.lucene.schema;

import com.code.a.sheep.codeasheep.interfaces.Schema;
import org.apache.lucene.document.Field;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the schema for our document.
 * This class is a clever {@link HashMap <String, LuceneField>}
 */

public class LuceneSchema extends Schema<LuceneField> {
    /**
     * Add a field in the schema
     *
     * @return the schema, for fluent calls
     */
    public LuceneSchema addField(LuceneField field) {
        this.addField(field.getName(), field);
        return this;
    }

    /**
     * Retrieves the list of inner fields really indexed by Lucene.
     * It can be text fields, doc values...
     *
     * @param field
     * @return
     */
    public List<Field> generateLuceneFields(Map.Entry<String, Object> field) {

        if (get(field.getKey()) == null) {
            throw new IllegalStateException("Field '" + field.getKey() + "' not found");
        }

        return get(field.getKey()).generateLuceneFields(field);
    }
}
