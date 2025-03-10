package ooo.autopo.model.po;

/*
 * This file is part of the Autopo project
 * Created 06/03/25
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

import com.soberlemur.potentilla.Catalog;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import ooo.autopo.model.LoadingStatus;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.requireNonNull;
import static org.sejda.commons.util.RequireUtils.requireArg;
import static org.sejda.commons.util.RequireUtils.requireNotNullArg;

/**
 * @author Andrea Vacondio
 */
public class PotFile {

    private final SimpleObjectProperty<LoadingStatus> status = new SimpleObjectProperty<>(LoadingStatus.INITIAL);
    private final AtomicReference<LoadingStatus> atomicState = new AtomicReference<>(LoadingStatus.INITIAL);
    private final Path potFile;
    private Catalog catalog;

    public PotFile(Path potFile) {
        requireNotNullArg(potFile, "The input .pot file cannot be null");
        this.potFile = potFile;
    }

    public Path potFile() {
        return potFile;
    }

    public Catalog catalog() {
        return catalog;
    }

    public void catalog(Catalog catalog) {
        requireNonNull(catalog);
        requireArg(catalog.isTemplate(), "Cannot set a non template catalog");
        this.catalog = catalog;
    }

    /**
     * Moves to the given loading status
     */
    public void status(LoadingStatus newStatus) {
        requireNonNull(newStatus);
        atomicState.set(newStatus);
        status.set(newStatus);
    }

    /**
     * Atomically sets the state if the current value is the expectedStatus {@link AtomicReference#compareAndSet}
     *
     * @return true if successful
     */
    public boolean status(LoadingStatus expectedStatus, LoadingStatus newStatus) {
        requireNonNull(newStatus);
        requireNonNull(expectedStatus);
        if (atomicState.compareAndSet(expectedStatus, newStatus)) {
            status.set(newStatus);
            return true;
        }
        return false;
    }

    public ObservableValue<LoadingStatus> status() {
        return this.status;
    }

    public void cancel() {
        this.atomicState.set(LoadingStatus.CANCELLED);
    }

    public boolean isCancelled() {
        return this.atomicState.get() == LoadingStatus.CANCELLED;
    }
}
