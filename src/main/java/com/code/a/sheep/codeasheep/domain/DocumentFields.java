package com.code.a.sheep.codeasheep.domain;

/**
 * Existing document field.
 */
public enum DocumentFields {

    /**
     * Text, the whole line
     */
    TEXT("text"),
    /**
     * Chapter of the line
     */
    CHAPTER("chapter"),
    /**
     * Is the line a dialog ?
     */
    IS_DIALOG("isDialog"),
    /**
     * Is the line a question ?
     */
    IS_QUESTION("isQuestion");

    DocumentFields(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }
}
