package com.code.a.sheep.codeasheep.plain;

import com.code.a.sheep.codeasheep.plain.index.PlainJavaDocumentStore;
import com.code.a.sheep.codeasheep.plain.index.PlainJavaFieldIndex;
import com.code.a.sheep.codeasheep.plain.index.PlainJavaIndex;
import com.code.a.sheep.codeasheep.plain.index.PlainJavaPostingList;
import com.code.a.sheep.codeasheep.plain.schema.PlainJavaSchema;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;


// Don't do that at home you naughty developer :D
public class ShamefulUtils {

    public static PlainJavaDocumentStore getInnerDocumentStore(PlainJavaDocumentIndexer plainJavaDocumentIndexer) {
        Field field = ReflectionUtils.findField(PlainJavaIndex.class, "documentStore");
        ReflectionUtils.makeAccessible(field);
        return (PlainJavaDocumentStore) ReflectionUtils.getField(field, plainJavaDocumentIndexer.getIndex());
    }

    public static PlainJavaSchema getSchema(PlainJavaIndex plainJavaIndex) {
        // Don't do that at home you naughty developer :D
        Field field = ReflectionUtils.findField(PlainJavaIndex.class, "schema");
        ReflectionUtils.makeAccessible(field);
        return (PlainJavaSchema) ReflectionUtils.getField(field, plainJavaIndex);
    }

    public static Map<Object, PlainJavaPostingList> retrievesFieldInvertedIndex(PlainJavaFieldIndex fieldIndex) {
        // Don't do that at home you naughty developer :D
        Field field = ReflectionUtils.findField(PlainJavaFieldIndex.class, "invertedIndex");
        ReflectionUtils.makeAccessible(field);
        return (Map<Object, PlainJavaPostingList>) ReflectionUtils.getField(field, fieldIndex);
    }
}
