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

import atlantafx.base.theme.Styles;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import ooo.autopo.app.ui.Style;

import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * @author Andrea Vacondio
 */
public class SettingsPane extends VBox {

    @Inject
    public SettingsPane(GeneralSettingsPane generalSettings, @Named("aiSettings") TabPane aiPane) {
        getStyleClass().add("spaced-container");
        var top = new VBox(Style.DEFAULT_SPACING);
        top.setMinWidth(USE_PREF_SIZE);
        addSectionTitle(i18n().tr("General settings"), top);
        top.getChildren().add(generalSettings);
        var bottom = new VBox(Style.DEFAULT_SPACING);
        bottom.setMinWidth(USE_PREF_SIZE);
        HBox.setHgrow(bottom, Priority.ALWAYS);
        addSectionTitle(i18n().tr("AI providers"), bottom);
        bottom.getChildren().add(aiPane);

        getChildren().addAll(top, bottom);
    }

    private void addSectionTitle(String title, Pane pane) {
        Label label = new Label(title);
        label.getStyleClass().add(Styles.TITLE_3);
        pane.getChildren().add(label);
    }
}
