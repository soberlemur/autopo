package ooo.autopo.app.ui;

/*
 * This file is part of the Autopo project
 * Created 14/02/25
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

import jakarta.inject.Inject;
import javafx.scene.control.SplitPane;
import ooo.autopo.app.ui.editor.EditorContainer;

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
