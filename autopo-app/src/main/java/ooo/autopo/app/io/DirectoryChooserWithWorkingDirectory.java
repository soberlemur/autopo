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

import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;
import static ooo.autopo.app.context.ApplicationContext.app;

/**
 * A DirectoryChooser that will open pointing to the latest know working directory
 *
 * @author Andrea Vacondio
 */
public class DirectoryChooserWithWorkingDirectory {
    private final DirectoryChooser wrapped = new DirectoryChooser();

    DirectoryChooserWithWorkingDirectory() {
        app().runtimeState().workingPath().subscribe(p -> wrapped.setInitialDirectory(ofNullable(p).map(Path::toFile).orElse(null)));
    }

    final void setTitle(String value) {
        wrapped.setTitle(value);
    }

    public final void setInitialDirectory(Path value) {
        wrapped.setInitialDirectory(ofNullable(value).map(Path::toFile).orElse(null));
    }

    public Path showDialog(Window ownerWindow) {
        if (ofNullable(wrapped.getInitialDirectory()).map(File::toPath).filter(not(Files::isDirectory)).isPresent()) {
            wrapped.setInitialDirectory(null);
        }
        Path selected = ofNullable(wrapped.showDialog(ownerWindow)).map(File::toPath).filter(Files::isDirectory).orElse(null);
        if (nonNull(selected)) {
            app().runtimeState().workingPath(selected);
        }
        return selected;
    }
}
