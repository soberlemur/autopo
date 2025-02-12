package ooo.autopo.model;

/*
 * This file is part of the Autopo project
 * Created 05/02/25
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.sejda.commons.util.RequireUtils.require;

/**
 * @author Andrea Vacondio
 */
public enum LoadingStatus {
    ERROR,
    LOADED,
    LOADING,
    INITIAL;

    static {
        INITIAL.setValidDestinationStatus(LOADING, ERROR);
        LOADING.setValidDestinationStatus(LOADED, ERROR);
    }

    private final Set<LoadingStatus> validNext = new HashSet<>();

    private void setValidDestinationStatus(LoadingStatus... next) {
        validNext.addAll(Arrays.asList(next));
    }

    /**
     * Moves the current status to the destination one if allowed
     *
     * @param dest
     * @return the destination status
     */
    public LoadingStatus moveTo(LoadingStatus dest) {
        require(validNext.contains(dest),
                () -> new IllegalStateException("Cannot move status from " + this + " to " + dest));
        return dest;
    }
}