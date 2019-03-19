package com.code.a.sheep.codeasheep.lucene.schema;

import lombok.Builder;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.SortedDocValues;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Representation of a {@link String} field in Lucene.
 */
public class LuceneTextField extends LuceneField<String> {

    @Builder
    public LuceneTextField(String name, boolean isStored, boolean withRawField) {
        super(name, isStored, withRawField);
    }

    /**
     * Generate Lucene fields
     *
     * @param field
     * @return
     */
    public List<Field> generateLuceneFields(Map.Entry<String, String> field) {
        String value = field.getValue();

        List<Field> luceneFields = new ArrayList<>();

        // Add a text field with its value in the index, store it if needed
        luceneFields.add(new TextField(field.getKey(), value, isStored() ? Field.Store.YES : Field.Store.NO));

        // If the field has a raw field, creates another field.
        // This new field is not a TextField, but a SortedDocValuesField
        if (this.isWithRawField()) {
            luceneFields.add(new SortedDocValuesField(getRawFieldName(field.getKey()), new BytesRef(field.getValue())));
        }

        return luceneFields;
    }

    @Override
    public DocIdSetIterator getDocIdSetIterator(LeafReader reader) throws IOException {
        return reader.getSortedDocValues(getRawFieldName(name));
    }

    @Override
    public String getDocValue(DocIdSetIterator docIdSetIterator) throws IOException {
        return ((SortedDocValues) docIdSetIterator).binaryValue().utf8ToString();
    }

    @Override
    public String getValue(IndexableField f) {
        return f.stringValue();
    }
}
