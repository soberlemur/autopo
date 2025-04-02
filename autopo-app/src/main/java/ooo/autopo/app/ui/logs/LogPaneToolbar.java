package ooo.autopo.app.ui.logs;
/*
 * This file is part of the Autopo project
 * Created 13/02/25
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
import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import ooo.autopo.app.ui.components.CloseOverlayButton;
import ooo.autopo.model.ui.log.ClearLogRequest;
import ooo.autopo.model.ui.log.SaveLogRequest;
import org.kordamp.ikonli.fluentui.FluentUiRegularAL;
import org.kordamp.ikonli.fluentui.FluentUiRegularMZ;
import org.kordamp.ikonli.javafx.FontIcon;

import static ooo.autopo.i18n.I18nContext.i18n;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * Toolbar for the log pane
 *
 * @author Andrea Vacondio
 */
class LogPaneToolbar extends ToolBar {

    @Inject
    public LogPaneToolbar(LogListView logView) {
        var clearItem = new ClearButton();
        clearItem.disableProperty().bind(Bindings.isEmpty(logView.getItems()));
        var saveItem = new SaveButton();
        saveItem.disableProperty().bind(clearItem.disableProperty());
        getItems().addAll(saveItem, clearItem, new CloseOverlayButton());
    }

    static class ClearButton extends Button {
        public ClearButton() {
            setTooltip(new Tooltip(i18n().tr("Removes all the log messages")));
            setText(i18n().tr("_Clear"));
            setGraphic(new FontIcon(FluentUiRegularAL.DELETE_20));
            getStyleClass().addAll(Styles.SMALL);
            setOnAction(e -> eventStudio().broadcast(new ClearLogRequest()));
        }
    }

    static class SaveButton extends Button {
        public SaveButton() {
            setText(i18n().tr("_Save"));
            setGraphic(new FontIcon(FluentUiRegularMZ.SAVE_20));
            getStyleClass().addAll(Styles.SMALL);
            setOnAction(e -> eventStudio().broadcast(new SaveLogRequest()));
        }
    }
}
