package com.theopus.xengine.utils;

import java.util.Comparator;
import java.util.TreeSet;

public class UpdatableTreeSet<E> extends TreeSet<E> {


    public UpdatableTreeSet(Comparator<? super E> comparator) {
        super(comparator);
    }

    /**
     * @return true if is component was in set
     */
    public boolean update(E element, Update<E> upd) {
        boolean remove = remove(element);
        if (remove) {
            upd.update(element);
            return add(element);
        } else {
            upd.update(element);
            add(element);
            return false;
        }
    }

    public interface Update<E> {
        void update(E t);
    }
}
