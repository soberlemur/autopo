package ooo.autopo.model.po;

/*
 * This file is part of the Autopo project
 * Created 31/01/25
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
import com.soberlemur.potentilla.Message;
import com.soberlemur.potentilla.MessageKey;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ooo.autopo.model.LoadingStatus;

import java.nio.file.Path;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.sejda.commons.util.RequireUtils.requireNotNullArg;
import static org.sejda.commons.util.RequireUtils.requireState;

/**
 * Represent a .po file.
 *
 * @author Andrea Vacondio
 */
public class PoFile {

    private final SimpleObjectProperty<LoadingStatus> status = new SimpleObjectProperty<>(LoadingStatus.INITIAL);
    private final AtomicReference<LoadingStatus> atomicState = new AtomicReference<>(LoadingStatus.INITIAL);
    private final Path poFile;
    private Locale locale;
    private Catalog catalog;
    private final ObservableList<PoEntry> entries = FXCollections.observableArrayList();
    private final SimpleDoubleProperty translationPercentage = new SimpleDoubleProperty(0);
    private final SimpleBooleanProperty modified = new SimpleBooleanProperty();

    public PoFile(Path poFile) {
        requireNotNullArg(poFile, "The input .po file cannot be null");
        this.poFile = poFile;
    }

    public Path poFile() {
        return poFile;
    }

    public Locale locale() {
        return locale;
    }

    /**
     * Sets the locale for this {@link PoFile } and notifies its entries. Entries need to be notified because a different locale may need different constraint
     * validators
     */
    public void locale(Locale locale) {
        requireNonNull(locale);
        this.locale = locale;
        this.entries.forEach(e -> e.notifyLocaleChange(locale));
    }

    public Catalog catalog() {
        return catalog;
    }

    /**
     * Sets the catalog for this PoFile. The Catalog is the structure resulting from the parsing of the .po file and contains all the {@link Message}s and
     * comments and header.
     */
    public void catalog(Catalog catalog) {
        requireNonNull(catalog);
        this.catalog = catalog;
        this.entries().clear();
        this.catalog.stream().filter(e -> !e.isObsolete()).map(PoEntry::new).forEachOrdered(e -> {
            this.addEntry(e);
            e.notifyLocaleChange(this.locale());
        });
    }

    public void addEntry(PoEntry entry) {
        requireNonNull(entry);
        entries.add(entry);
    }

    public ObservableList<PoEntry> entries() {
        return entries;
    }

    public ObservableDoubleValue translationPercentage() {
        return translationPercentage;
    }

    /**
     * Walk through all the entries to see what is translated and what is not, updating the translationPercentage observable value
     */
    public void updatePercentageOfTranslation() {
        var translated = entries().stream().filter(t -> isNotBlank(t.translatedValue().getValue())).count();
        if (translated == 0) {
            translationPercentage.set(0);
        } else {
            translationPercentage.set((double) translated / entries.size());
        }
    }

    /**
     * Clears the obsolete messages from the catalog. No need to check the entries collection since we don't add obsolete entries there.
     *
     * @return true if some obsolete entry was found and removed
     */
    public boolean clearObsolete() {
        requireState(isLoaded(), "Cannot clear obsolete messages from a non loaded po file");
        var obsolete = catalog.stream().filter(Message::isObsolete).map(MessageKey::new).toList();
        if (!obsolete.isEmpty()) {
            obsolete.forEach(catalog::remove);
            this.modified(true);
            return true;
        }
        return false;
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

    public void cancel() {
        this.atomicState.set(LoadingStatus.CANCELLED);
    }

    public boolean isLoaded() {
        return this.atomicState.get() == LoadingStatus.LOADED;
    }

    public ObservableBooleanValue modifiedProperty() {
        return modified;
    }

    public void modified(boolean value) {
        this.modified.set(value);
    }

    /**
     * Updates the catalog from the given template. This will add to this po any new entry in the pot and mark obsolete any entry that is not in the pot
     * anymore
     *
     * @param pot
     */
    public void updateFromTemplate(PotFile pot) {
        this.catalog.updateFromTemplate(pot.catalog());
        //refresh the catalog
        this.catalog(this.catalog);
    }
}
