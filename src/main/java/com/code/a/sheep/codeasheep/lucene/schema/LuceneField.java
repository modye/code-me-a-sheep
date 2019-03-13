package com.code.a.sheep.codeasheep.lucene.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.search.DocIdSetIterator;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * This class represents a field in Lucene
 *
 * @param <T>
 */
@AllArgsConstructor
@Data
public abstract class LuceneField<T> {

    /**
     * Suffix used for additional raw fields.
     * Raw field for field "foo" will be "foo.raw"
     */
    private static final String RAW_SUFFIX = ".raw";
    /**
     * Name of the field
     */
    protected final String name;
    /**
     * Is the field stored ?
     */
    private final boolean isStored;
    /**
     * Does the field have an additional raw field ?
     */
    private final boolean withRawField;

    /**
     * Generate the fields from a map, the map represents the configuration of the fields
     *
     * @param fieldsConfiguration
     * @return a list of fields
     */
    public abstract List<Field> generateLuceneFields(Map.Entry<String, T> fieldsConfiguration);

    /**
     * Return the raw name of a field.
     * Raw field for field "foo" will be "foo.raw"
     *
     * @param fieldName
     * @return
     */
    public String getRawFieldName(String fieldName) {
        return fieldName + RAW_SUFFIX;
    }

    public abstract DocIdSetIterator getDocIdSetIterator(LeafReader reader) throws IOException;

    public abstract T getDocValue(DocIdSetIterator docIdSetIterator) throws IOException;

    public abstract T getValue(IndexableField f);
}
