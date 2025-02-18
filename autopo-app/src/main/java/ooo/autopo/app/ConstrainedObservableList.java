package ooo.autopo.app;
/*
 * This file is part of the Autopo project
 * Created 17/02/25
 * Copyright 2025 by Sober Lemur S.r.l. (info@soberlemur.com).
 *
 * You are not permitted to distribute it in any form unless explicit
 * consent is given by Sober Lemur S.r.l..
 * You are not permitted to modify it.
 *
 * Autopo is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

import javafx.collections.ModifiableObservableListBase;

import java.util.Collection;
import java.util.LinkedList;

import static org.sejda.commons.util.RequireUtils.requireArg;

/**
 * A {@link ModifiableObservableListBase} with size constraints. When at maxCapacity and an element is added, the eldest element is removed.
 *
 * @author Andrea Vacondio
 */
public class ConstrainedObservableList<E> extends ModifiableObservableListBase<E> {
    private final LinkedList<E> wrapped;
    private int maxCapacity;

    public ConstrainedObservableList(int maxCapacity) {
        this.wrapped = new LinkedList<>();
        setMaxCapacity(maxCapacity);
    }

    public void setMaxCapacity(int maxCapacity) {
        requireArg(maxCapacity > 0, "Max capacity must be a positive value");
        this.maxCapacity = maxCapacity;
        houseKeep();
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Makes the list fit its limits by removing last items in cases where the list might have exceeded its limits.
     */
    private void houseKeep() {
        while (size() > maxCapacity) {
            remove(0);
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean retVal;
        try {
            beginChange();
            retVal = wrapped.addAll(index, c);
            nextAdd(index, index + c.size());
            houseKeep();
            modCount++;
        } finally {
            endChange();
        }
        return retVal;
    }

    @Override
    public E get(int index) {
        return wrapped.get(index);
    }

    @Override
    public int size() {
        return wrapped.size();
    }

    @Override
    protected void doAdd(int index, E element) {
        wrapped.add(index, element);
        houseKeep();
    }

    @Override
    protected E doSet(int index, E element) {
        return wrapped.set(index, element);
    }

    @Override
    protected E doRemove(int index) {
        return wrapped.remove(index);
    }

}
