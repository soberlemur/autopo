package ooo.autopo.app.ui;

/*
 * This file is part of the Autopo project
 * Created 14/02/25
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

import jakarta.inject.Inject;
import javafx.scene.control.SplitPane;
import ooo.autopo.app.ui.editor.EditorContainer;
import ooo.autopo.app.ui.explorer.FileExplorer;

/**
 * @author Andrea Vacondio
 */
public class MainPane extends SplitPane {
    @Inject
    public MainPane(FileExplorer fileExplorer, EditorContainer editor) {
        this.getStyleClass().add("main-pane");
        this.getItems().addAll(fileExplorer, editor);
    }
}
