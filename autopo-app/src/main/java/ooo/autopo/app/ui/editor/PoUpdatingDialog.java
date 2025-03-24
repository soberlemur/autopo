package ooo.autopo.app.ui.editor;

/*
 * This file is part of the Autopo project
 * Created 24/03/25
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
import org.kordamp.ikonli.fluentui.FluentUiRegularAL;
import org.kordamp.ikonli.javafx.FontIcon;

import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * @author Andrea Vacondio
 */
public class PoUpdatingDialog
        extends VBox {

    public PoUpdatingDialog() {
        getStyleClass().add("autopo-dialog");
        setSpacing(10);
        setAlignment(Pos.CENTER);
        setMinSize(350, 200);
        setMaxSize(350, 200);
        var label = new Label(i18n().tr("Updating..."), new FontIcon(FluentUiRegularAL.ARROW_SYNC_24));
        var progress = new ProgressBar();
        progress.setPrefWidth(200);
        getChildren().addAll(label, progress);
    }
}
