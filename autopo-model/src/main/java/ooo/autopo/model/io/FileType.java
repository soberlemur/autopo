package ooo.autopo.model.io;
/*
 * This file is part of the Autopo project
 * Created 17/02/25
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

import javafx.stage.FileChooser.ExtensionFilter;
import org.apache.commons.io.IOCase;

import static org.apache.commons.io.FilenameUtils.wildcardMatch;

/**
 * @author Andrea Vacondio
 */
public enum FileType {
    ALL("(*.*)", "*.*", "*"),
    OOO("(*.ooo)", "*.ooo", "*.OOO"),
    POT("(*.pot)", "*.pot", "*.POT"),
    TXT("(*.txt)", "*.txt", "*.TXT"),
    LOG("(*.log, *.txt)", "*.log", "*.txt", "*.LOG", "*.TXT"),
    PO("(*.po)", "*.po", "*.PO");

    private final ExtensionFilter filter;

    FileType(String description, String... extensions) {
        this.filter = new ExtensionFilter(description, extensions);
    }

    public ExtensionFilter getFilter() {
        return filter;
    }

    /**
     * @param filename
     * @return true if the input filename is of this {@link FileType}
     */
    public boolean matches(String filename) {
        return getFilter().getExtensions().stream().anyMatch(ext -> wildcardMatch(filename, ext, IOCase.INSENSITIVE));

    }
}
