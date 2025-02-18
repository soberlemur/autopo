package ooo.autopo.app.ui.settings;

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

import jakarta.inject.Inject;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ooo.autopo.app.ui.Style;

import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * @author Andrea Vacondio
 */
public class SettingsPane extends HBox {

    @Inject
    public SettingsPane(SettingsAppearencePane appearance) {
        getStyleClass().add("spaced-container");
        VBox left = new VBox(Style.DEFAULT_SPACING);
        left.setMinWidth(USE_PREF_SIZE);
        addSectionTitle(i18n().tr("Appearance"), left);
        left.getChildren().add(appearance);
        getChildren().addAll(left);
    }

    private void addSectionTitle(String title, Pane pane) {
        Label label = new Label(title);
        label.getStyleClass().add("section-title");
        pane.getChildren().add(label);
    }
}
