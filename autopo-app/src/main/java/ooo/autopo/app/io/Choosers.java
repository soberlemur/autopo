package ooo.autopo.app.io;

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

import ooo.autopo.model.io.FileType;

/**
 * @author Andrea Vacondio
 */
public final class Choosers {
    private Choosers() {
        // hide
    }

    /**
     * @param filters the file type extension accepted
     * @return a shared instance of {@link javafx.stage.FileChooser} with the given title.
     */
    public static FileChooserWithWorkingDirectory fileChooser(String title, FileType... filters) {
        FileChooserHolder.FILE_CHOOSER.setFileTypes(filters);
        FileChooserHolder.FILE_CHOOSER.setInitialFileName("");
        FileChooserHolder.FILE_CHOOSER.setTitle(title);
        return FileChooserHolder.FILE_CHOOSER;
    }

    /**
     * @return a shared instance of a {@link javafx.stage.DirectoryChooser} wrapper with the given title.
     */
    public static DirectoryChooserWithWorkingDirectory directoryChooser(String title) {
        DirectoryChooserHolder.DIRECTORY_CHOOSER.setTitle(title);
        return DirectoryChooserHolder.DIRECTORY_CHOOSER;
    }

    /**
     * Lazy initialization holder class idiom (Joshua Bloch, Effective Java second edition, item 71).
     *
     * @author Andrea Vacondio
     */
    private static final class FileChooserHolder {

        private FileChooserHolder() {
            // hide constructor
        }

        static final FileChooserWithWorkingDirectory FILE_CHOOSER = new FileChooserWithWorkingDirectory();
    }

    private static final class DirectoryChooserHolder {

        private DirectoryChooserHolder() {
            // hide constructor
        }

        static final DirectoryChooserWithWorkingDirectory DIRECTORY_CHOOSER = new DirectoryChooserWithWorkingDirectory();
    }
}
