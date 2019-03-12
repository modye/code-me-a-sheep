package com.code.a.sheep.codeasheep.plain.index;

import java.util.TreeSet;

/**
 * This dummy posting list contains document ids.
 * Usually, posting lists are compressed.
 * This really simple implementation is for educational purpose only.
 */
public class PlainJavaPostingList extends TreeSet<Integer> implements Comparable<PlainJavaPostingList> {

    private int maxSize;
    private String searchedField;

    public void commit() {
        maxSize = size();
        //sort(Comparator.naturalOrder());
    }

    public int getMaxSize() {
        return maxSize;
    }

    public String getSearchedField() {
        return searchedField;
    }

    public void setSearchedField(String searchedField) {
        this.searchedField = searchedField;
    }

    /**
     * used during merging
     */
    @Override
    public int compareTo(PlainJavaPostingList o) {
        return Integer.compare(this.isEmpty() ? 0 : this.first(), o.isEmpty() ? 0 : o.first());
    }
}
