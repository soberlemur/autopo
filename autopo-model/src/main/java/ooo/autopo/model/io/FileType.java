package ooo.autopo.model.io;
/*
 * This file is part of the Autopo project
 * Created 17/02/25
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
