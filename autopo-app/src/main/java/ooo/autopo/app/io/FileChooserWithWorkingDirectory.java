/*
 * This file is part of the PDF Black project
 * Created on 16 set 2020
 * Copyright 2020 by Sober Lemur S.r.l. (info@soberlemur.com).
 *
 * You are not permitted to distribute it in any form unless explicit
 * consent is given by Sober Lemur S.r.l..
 * You are not permitted to modify it.
 *
 * PDF Black is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package ooo.autopo.app.io;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import ooo.autopo.model.io.FileType;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;
import static ooo.autopo.app.context.ApplicationContext.app;

/**
 * A FileChooser that will open pointing to the latest know working directory and can be configured to open single/multiple files or show a save dialog
 *
 * @author Andrea Vacondio
 */
public class FileChooserWithWorkingDirectory {
    private final FileChooser wrapped = new FileChooser();

    FileChooserWithWorkingDirectory() {
        app().runtimeState().workingPath().subscribe(p -> wrapped.setInitialDirectory(ofNullable(p).map(Path::toFile).orElse(null)));
    }

    final void setTitle(String value) {
        wrapped.setTitle(value);
    }

    public final void setInitialFileName(String value) {
        wrapped.setInitialFileName(value);
    }

    private void sanitizeInitialDirectory() {
        if (ofNullable(wrapped.getInitialDirectory()).map(File::toPath).filter(not(Files::isDirectory)).isPresent()) {
            wrapped.setInitialDirectory(null);
        }
    }

    /**
     * Shows a dialog to select multiple files
     *
     * @param ownerWindow
     * @return
     */
    public List<Path> showOpenMultipleDialog(Window ownerWindow) {
        sanitizeInitialDirectory();
        List<Path> selected = ofNullable(wrapped.showOpenMultipleDialog(ownerWindow)).map(f -> f.stream()
                                                                                                .map(File::toPath)
                                                                                                .filter(Files::isRegularFile)
                                                                                                .toList()).orElse(emptyList());
        selected.stream().findFirst().ifPresent(this::updateWorkingPath);
        return selected;
    }

    void setFileTypes(FileType... filters) {
        wrapped.getExtensionFilters().setAll(stream(filters).map(FileType::getFilter).toList());
    }

    private Path updateWorkingPath(Path path) {
        if (nonNull(path)) {
            app().runtimeState().workingPath(path);
        }
        return path;
    }

    private Path singleFileDialog(File file) {
        return ofNullable(file).map(File::toPath)
                               .filter(f -> !Files.isDirectory(f))
                               .map(this::updateWorkingPath)
                               .orElse(null);

    }

    /**
     * Shows a dialog to select/open a single file
     *
     * @param ownerWindow
     * @return
     */
    public Path showOpenSingleDialog(Window ownerWindow) {
        sanitizeInitialDirectory();
        return singleExistingFileDialog(wrapped.showOpenDialog(ownerWindow));
    }

    private Path singleExistingFileDialog(File file) {
        return ofNullable(file).map(File::toPath)
                               .filter(Files::isRegularFile)
                               .map(this::updateWorkingPath)
                               .orElse(null);

    }

    /**
     * Shows a dialog to select/save a single file
     *
     * @param ownerWindow
     * @return
     */
    public Path showSaveDialog(Window ownerWindow) {
        sanitizeInitialDirectory();
        return singleFileDialog(wrapped.showSaveDialog(ownerWindow));
    }

    /**
     * Shows a dialog to select a single file or multiple files
     *
     * @param ownerWindow
     * @param multiple
     * @return
     */
    public List<Path> showOpenDialog(Window ownerWindow, boolean multiple) {
        if (!multiple) {
            return List.of(showOpenSingleDialog(ownerWindow));
        }
        return showOpenMultipleDialog(ownerWindow);
    }

}
