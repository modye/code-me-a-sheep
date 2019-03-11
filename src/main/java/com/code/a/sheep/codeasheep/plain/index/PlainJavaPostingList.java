package com.code.a.sheep.codeasheep.plain.index;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * This dummy posting list contains document ids.
 * Usually, posting lists are compressed.
 * This really simple implementation is for educational purpose only.
 *
 */
public class PlainJavaPostingList extends ArrayList<Integer> implements Comparable<PlainJavaPostingList> {

    private int maxSize;

    public void commit() {
        maxSize = size();
        sort(Comparator.naturalOrder());
    }

    public int getMaxSize() {
        return maxSize;
    }

    /**
     * used during merging
     *
     */
    @Override
    public int compareTo(PlainJavaPostingList o) {
        return Integer.compare(this.isEmpty() ? 0 : this.get(0), o.isEmpty() ? 0 : o.get(0));
    }
}
