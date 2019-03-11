package com.code.a.sheep.codeasheep.plain.schema;

import com.code.a.sheep.codeasheep.Schema;

public class PlainJavaSchema extends Schema<PlainJavaField> {

    /**
     * Add a field in the schema
     *
     * @return the schema, for fluent calls
     */
    public PlainJavaSchema addField(PlainJavaField field) {
        this.addField(field.getName(), field);
        return this;
    }
}
