package ooo.autopo.app.ui.editor;

/*
 * This file is part of the Autopo project
 * Created 20/03/25
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

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

/**
 * @author Andrea Vacondio
 */
public class WaitDialog extends VBox {

    public WaitDialog() {
        getStyleClass().add("autopo-dialog");
        setSpacing(10);
        setAlignment(Pos.CENTER);
        setMinSize(350, 200);
        setMaxSize(350, 200);
        var progress = new ProgressBar();
        progress.setPrefWidth(200);
        getChildren().addAll(new Label(), progress);
    }

    public void setLabel(Label label) {
        getChildren().set(0, label);
    }
}
