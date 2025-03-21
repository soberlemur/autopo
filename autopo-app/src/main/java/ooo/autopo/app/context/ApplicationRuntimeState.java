package ooo.autopo.app.context;

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
import javafx.beans.value.ObservableValue;
import ooo.autopo.model.ai.AIModelDescriptor;
import ooo.autopo.model.po.PoEntry;
import ooo.autopo.model.po.PoFile;
import ooo.autopo.model.project.Project;
import ooo.autopo.theme.Theme;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;

/**
 * @author Andrea Vacondio
 */
public class ApplicationRuntimeState {

    private final SimpleObjectProperty<Theme> theme = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Path> workingPath = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Project> project = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<PoFile> poFile = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<PoEntry> poEntry = new SimpleObjectProperty<>();
    private final Map<String, AIModelDescriptor> aiModels;

    ApplicationRuntimeState() {
        aiModels = ServiceLoader.load(AIModelDescriptor.class)
                                .stream()
                                .map(java.util.ServiceLoader.Provider::get)
                                .collect(Collectors.toMap(AIModelDescriptor::id, identity()));
    }

    /**
     * @return the current theme
     */
    public ObservableValue<Theme> theme() {
        return this.theme;
    }

    /**
     * Sets the application theme
     */
    public void theme(Theme theme) {
        ofNullable(theme).ifPresent(this.theme::set);
    }

    /**
     * @return the current project
     */
    public ObservableObjectValue<Project> project() {
        return this.project;
    }

    /**
     * Sets the current project
     */
    public void project(Project project) {
        if (nonNull(project)) {
            ofNullable(this.project.get()).ifPresent(p -> {
                p.translations().forEach(PoFile::cancel);
            });
            this.project.set(project);
            this.poFile.set(null);
            this.poEntry.set(null);
        }
    }

    /**
     * @return the current .po file
     */
    public ObservableObjectValue<PoFile> poFile() {
        return this.poFile;
    }

    /**
     * Sets the current .po file
     */
    public void poFile(PoFile poFile) {
        if (nonNull(poFile)) {
            this.poFile.set(poFile);
            this.poEntry.set(null);
        }
    }

    /**
     * @return the current entry in the current .po file
     */
    public ObservableObjectValue<PoEntry> poEntry() {
        return this.poEntry;
    }

    /**
     * Sets the current po entry
     */
    public void poEntry(PoEntry poEntry) {
        this.poEntry.set(poEntry);
    }

    /**
     * Sets the current working path for the application
     *
     * @param path a valid path string. A blank or null or non directory value clears the current working path
     */
    public void workingPath(String path) {
        this.workingPath(ofNullable(path).filter(StringUtils::isNotBlank).map(Paths::get).orElse(null));
    }

    /**
     * Sets the current working path for the application
     *
     * @param path the current working directory or the parent in case of regular file. A null value clears the current working path
     */
    public void workingPath(Path path) {
        workingPath.set(ofNullable(path).map(p -> {
            if (Files.isRegularFile(p)) {
                return p.getParent();
            }
            return p;
        }).filter(Files::isDirectory).orElse(null));
    }

    public ObservableValue<Path> workingPath() {
        return workingPath;
    }

    /**
     * @return the available models
     */
    public Collection<AIModelDescriptor> aiModels() {
        return aiModels.values();
    }
}
