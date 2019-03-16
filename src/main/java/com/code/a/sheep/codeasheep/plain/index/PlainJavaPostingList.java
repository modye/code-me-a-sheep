package com.code.a.sheep.codeasheep.plain.index;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.ListIterator;

/**
 * This dummy posting list contains document ids.
 * Usually, posting lists are compressed.
 * This really simple implementation is for educational purpose only.
 */
public class PlainJavaPostingList extends ArrayList<Integer> {

    private int maxSize;
    private String searchedField;

    public void commit() {
        maxSize = size();
        sort(Comparator.naturalOrder());
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


    @Override
    public ListIterator<Integer> listIterator() {
        return new PostingIterator(super.listIterator(), this);
    }

    @Override
    public ListIterator<Integer> listIterator(int index) {
        return new PostingIterator(super.listIterator(index), this);
    }

    /**
     * Delegated iterator with a {@link Comparable} implementation for merging purposes
     */
    public static class PostingIterator implements ListIterator<Integer>, Comparable<PostingIterator> {
        private final ListIterator<Integer> innerIterator;
        private PlainJavaPostingList postingList;

        private PostingIterator(ListIterator<Integer> innerIterator, PlainJavaPostingList postingList) {
            this.innerIterator = innerIterator;
            this.postingList = postingList;
        }

        @Override
        public boolean hasNext() {
            return innerIterator.hasNext();
        }

        @Override
        public Integer next() {
            return innerIterator.next();
        }

        @Override
        public boolean hasPrevious() {
            return innerIterator.hasPrevious();
        }

        @Override
        public Integer previous() {
            return innerIterator.previous();
        }

        @Override
        public int nextIndex() {
            return innerIterator.nextIndex();
        }

        @Override
        public int previousIndex() {
            return innerIterator.previousIndex();
        }

        @Override
        public void remove() {
            throw new IllegalStateException("Not available.");
        }

        @Override
        public void set(Integer integer) {
            throw new IllegalStateException("Not available.");
        }

        @Override
        public void add(Integer integer) {
            throw new IllegalStateException("Not available.");
        }

        public Integer checkNext() {
            return innerIterator.hasNext() ? postingList.get(innerIterator.nextIndex()) : null;
        }

        @Override
        public int compareTo(PostingIterator o) {
            Integer nextDocumentId = checkNext();
            if (nextDocumentId == null) {
                nextDocumentId = Integer.MAX_VALUE;
            }

            Integer otherNextDocumentId = o.checkNext();
            if (otherNextDocumentId == null) {
                otherNextDocumentId = Integer.MAX_VALUE;
            }


            return Integer.compare(nextDocumentId, otherNextDocumentId);
        }

        public int getMaxSize() {
            return postingList.getMaxSize();
        }

        public String getSearchedField() {
            return postingList.getSearchedField();
        }

        public void setSearchedField(String searchField) {
            postingList.setSearchedField(searchField);
        }
    }
}
