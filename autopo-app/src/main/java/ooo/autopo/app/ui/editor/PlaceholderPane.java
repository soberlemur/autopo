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

import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * @author Andrea Vacondio
 */
public class PlaceholderPane extends VBox {

    public PlaceholderPane() {
        this.getStyleClass().addAll(Tweaks.EDGE_TO_EDGE, "placeholder-pane");
        var label = new Label("No translation selected");
        label.getStyleClass().addAll(Styles.TITLE_1, "placeholder");
        getChildren().add(label);
    }
}
