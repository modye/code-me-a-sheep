package com.code.a.sheep.codeasheep.lucene.schema;

import lombok.Builder;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.search.DocIdSetIterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Representation of a {@link Boolean} field in Lucene.
 */
public class LuceneBooleanField extends LuceneField<Boolean> {

    /**
     * Suffix used to create another field using fast search
     */
    private static final String FAST_SUFFIX = ".fast";

    @Builder
    public LuceneBooleanField(String name, boolean isStored, boolean withRawField) {
        super(name, isStored, withRawField);
    }

    /**
     * Generate Lucene fields
     *
     * @param field
     * @return
     */
    public List<Field> generateLuceneFields(Map.Entry<String, Boolean> field) {
        List<Field> luceneFields = new ArrayList<>();
        int value = field.getValue() ? 1 : 0;


        // Add an int field with its value in the index for fast search
        luceneFields.add(new IntPoint(field.getKey() + FAST_SUFFIX, value));

        // If the field has a raw field, creates another field.
        // This new field is not a TextField, but a NumericDocValuesField
        if (isWithRawField()) {
            luceneFields.add(new NumericDocValuesField(getRawFieldName(field.getKey()), value));
        }

        // Is the fields must be stored, store it
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
