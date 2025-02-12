package ooo.autopo.model;

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
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.Locale;

import static java.util.Objects.requireNonNull;
import static org.sejda.commons.util.RequireUtils.requireNotNullArg;

/**
 * @author Andrea Vacondio
 */
public class PoFile {

    private final SimpleObjectProperty<LoadingStatus> status = new SimpleObjectProperty<>(LoadingStatus.INITIAL);
    private File poFile;
    private Locale locale;
    private Catalog catalog;
    private ObservableList<PoEntry> entries = FXCollections.observableArrayList();

    public PoFile(File poFile) {
        requireNotNullArg(poFile, "The input .po file cannot be null");
        this.poFile = poFile;

    }

    public File poFile() {
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
        this.catalog.stream().map(PoEntry::new).forEachOrdered(e -> {
            this.entries().add(e);
            e.onLocaleUpdate(this.locale());
        });
    }

    public void addEntry(PoEntry entry) {
        requireNonNull(entry);
        entries.add(entry);
    }

    //TODO verify if we need an immutable view or if it needs to be observable at all
    private ObservableList<PoEntry> entries() {
        return entries;
    }

    /**
     * Moves to the given loading status
     */
    public void moveStatusTo(LoadingStatus newStatus) {
        requireNonNull(newStatus);
        status.set(status.getValue().moveTo(newStatus));
    }

    public ObservableValue<LoadingStatus> status() {
        return this.status;
    }
}
