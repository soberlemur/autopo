package ooo.autopo.app.ui.editor;

/*
 * This file is part of the Autopo project
 * Created 25/02/25
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
import javafx.beans.binding.Bindings;
import javafx.scene.layout.StackPane;

import static ooo.autopo.app.context.ApplicationContext.app;

/**
 * @author Andrea Vacondio
 */
public class EditorContainer extends StackPane {

    @Inject
    public EditorContainer(PlaceholderPane placeholderPane, EditorPane editorPane) {
        //whenever a .po file is not selected the placeholder is made visible
        placeholderPane.visibleProperty().bind(Bindings.isNull(app().runtimeState().poFile()));
        editorPane.visibleProperty().bind(Bindings.isNotNull(app().runtimeState().poFile()));
        this.getChildren().addAll(editorPane, placeholderPane);
    }

}
