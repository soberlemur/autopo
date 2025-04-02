package ooo.autopo.app.ui.settings;

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
