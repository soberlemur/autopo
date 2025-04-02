package ooo.autopo.model.po;

/*
 * This file is part of the Autopo project
 * Created 06/03/25
 * Copyright 2025 by Sober Lemur S.r.l. (info@soberlemur.com).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import com.soberlemur.potentilla.Catalog;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import ooo.autopo.model.LoadingStatus;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.requireNonNull;
import static org.sejda.commons.util.RequireUtils.requireArg;
import static org.sejda.commons.util.RequireUtils.requireNotNullArg;

/**
 * Represent a .pot file.
 *
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

    /**
     * sets a template catalog
     *
     * @throws IllegalArgumentException if the input catalog is not a template
     */
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

    public ObservableObjectValue<LoadingStatus> status() {
        return this.status;
    }

    public boolean isLoaded() {
        return this.atomicState.get() == LoadingStatus.LOADED;
    }
}
