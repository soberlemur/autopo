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

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import ooo.autopo.app.ui.logs.ErrorLoggedEvent;
import ooo.autopo.model.io.IOEvent;
import ooo.autopo.model.ui.SetOverlayItem;
import ooo.autopo.model.ui.SetStatusLabelRequest;
import org.kordamp.ikonli.fluentui.FluentUiRegularAL;
import org.kordamp.ikonli.javafx.FontIcon;
import org.pdfsam.eventstudio.annotation.EventListener;

import static ooo.autopo.i18n.I18nContext.i18n;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
public class FooterBar extends HBox {

    private final Label status = new Label();

    public FooterBar() {
        this.getStyleClass().add("footer");
        var spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        var logsButton = new Button();
        logsButton.setGraphic(new FontIcon(FluentUiRegularAL.DOCUMENT_ONE_PAGE_24));
        logsButton.getStyleClass().addAll(Styles.SMALL, Styles.FLAT);
        logsButton.setOnAction(e -> eventStudio().broadcast(new SetOverlayItem("LOGS")));
        logsButton.setTooltip(new Tooltip(i18n().tr("Open application logs")));
        eventStudio().add(ErrorLoggedEvent.class, e -> logsButton.getStyleClass().add(Styles.DANGER));
        eventStudio().add(SetOverlayItem.class, e -> {
            if ("LOGS".equals(e.id())) {
                logsButton.getStyleClass().remove(Styles.DANGER);
            }
        });

        this.getChildren().addAll(status, spacer, logsButton);
        eventStudio().addAnnotatedListeners(this);
    }

    @EventListener
    public void onStatus(SetStatusLabelRequest status) {
        setStatusMessage(status.status());
    }

    @EventListener
    public void onIOEvent(IOEvent event) {
        switch (event.type()) {
        case SAVED -> setStatusMessage(i18n().tr("File {0} saved", event.path().toString()));
        case LOADED -> setStatusMessage(i18n().tr("File {0} loaded", event.path().toString()));
        }
    }

    private void setStatusMessage(String message) {
        Platform.runLater(() -> this.status.setText(message));
    }
}
