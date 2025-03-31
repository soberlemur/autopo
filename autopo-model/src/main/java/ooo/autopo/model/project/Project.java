package ooo.autopo.model.project;

/*
 * This file is part of the Autopo project
 * Created 13/02/25
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

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import ooo.autopo.model.LoadingStatus;
import ooo.autopo.model.po.PoFile;
import ooo.autopo.model.po.PotFile;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

import static java.util.Objects.requireNonNull;

/**
 * Model for a project. A project has a root directory, a template (.pot) inside the root or a subdirectory (but not outside the root), a loading status and a
 * number of translations inside the root or its subdirectories
 *
 * @author Andrea Vacondio
 */
public class Project {

    private final Path location;
    private final Properties properties = new Properties();
    private final SimpleObjectProperty<PotFile> pot = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<LoadingStatus> status = new SimpleObjectProperty<>(LoadingStatus.INITIAL);
    private final SortedSet<PoFile> translations = new TreeSet<>(Comparator.comparing(PoFile::poFile));

    public Project(Path location) {
        this.location = location;
    }

    /**
     * @return the value for the property or null
     */
    public String getProperty(ProjectProperty property) {
        return properties.getProperty(property.key());
    }

    public void setProperty(ProjectProperty property, String value) {
        properties.setProperty(property.key(), value);
    }

    /**
     * Moves to the given loading status
     */
    public void status(LoadingStatus newStatus) {
        requireNonNull(newStatus);
        status.set(newStatus);
    }

    /**
     * @return the loading status. It's LoadingStatus.LOADED once the IOService as walked all the root subdirectories searching for pot and po files.
     */
    public ObservableObjectValue<LoadingStatus> status() {
        return this.status;
    }

    public Path location() {
        return location;
    }

    public Properties properties() {
        return properties;
    }

    public SortedSet<PoFile> translations() {
        return translations;
    }

    public void addTranslation(PoFile poFile) {
        this.translations.add(poFile);
    }

    /**
     * Assign the given template to the project.
     *
     * @param pot
     */
    public void pot(Path pot) {
        setProperty(ProjectProperty.TEMPLATE_PATH, pot.toAbsolutePath().toString());
        this.pot.set(new PotFile(pot));
    }

    public ObservableObjectValue<PotFile> pot() {
        return this.pot;
    }
}
