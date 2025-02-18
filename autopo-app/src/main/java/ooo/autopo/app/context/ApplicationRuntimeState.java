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
import javafx.beans.value.ObservableValue;
import ooo.autopo.theme.Theme;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Optional.ofNullable;

/**
 * @author Andrea Vacondio
 */
public class ApplicationRuntimeState {

    private final SimpleObjectProperty<Theme> theme = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Path> workingPath = new SimpleObjectProperty<>();

    /**
     * @return the current theme
     */
    public ObservableValue<Theme> theme() {
        return this.theme;
    }

    /**
     * Sets the application scale
     */
    public void theme(Theme theme) {
        ofNullable(theme).ifPresent(this.theme::set);
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
}
