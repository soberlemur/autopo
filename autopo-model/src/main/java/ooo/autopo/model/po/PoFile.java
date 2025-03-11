package ooo.autopo.model.po;

/*
 * This file is part of the Autopo project
 * Created 31/01/25
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
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ooo.autopo.model.LoadingStatus;

import java.nio.file.Path;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.sejda.commons.util.RequireUtils.requireNotNullArg;

/**
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

    public void locale(Locale locale) {
        requireNonNull(locale);
        this.locale = locale;
        this.entries.forEach(e -> e.onLocaleUpdate(locale));
    }

    public Catalog catalog() {
        return catalog;
    }

    public void catalog(Catalog catalog) {
        requireNonNull(catalog);
        this.catalog = catalog;
        this.entries().clear();
        this.catalog.stream().filter(e -> !e.isObsolete()).map(PoEntry::new).forEachOrdered(e -> {
            this.addEntry(e);
            e.onLocaleUpdate(this.locale());
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

    public void updatePercentageOfTranslation() {
        var translated = entries().stream().filter(t -> isNotBlank(t.translatedValue().getValue())).count();
        translationPercentage.set((double) translated / entries.size());
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

    public boolean isLoaded() {
        return this.atomicState.get() == LoadingStatus.LOADED;
    }

    public ObservableBooleanValue modifiedProperty() {
        return modified;
    }

    public void modified(boolean value) {
        this.modified.set(value);
    }

    public void updateFromTemplate(PotFile pot) {
        //TODO require not modified?
        this.catalog.updateFromTemplate(pot.catalog());
        //refresh the catalog
        this.catalog(this.catalog);
    }
}
