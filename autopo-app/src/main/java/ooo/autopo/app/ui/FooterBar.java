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

import atlantafx.base.theme.Styles;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import ooo.autopo.model.ui.SetOverlayItem;
import org.kordamp.ikonli.fluentui.FluentUiRegularAL;
import org.kordamp.ikonli.javafx.FontIcon;

import static ooo.autopo.i18n.I18nContext.i18n;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
public class FooterBar extends ToolBar {

    private final Label status = new Label();

    public FooterBar() {
        this.getStyleClass().add("footer");
        //  this.status.getStyleClass().add("status-label");
        this.status.setText("Ready");
        var spacer = new Pane();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        var logsButton = new Button();
        logsButton.setGraphic(new FontIcon(FluentUiRegularAL.DOCUMENT_ONE_PAGE_24));
        logsButton.getStyleClass().addAll(Styles.SMALL);
        logsButton.setOnAction(e -> eventStudio().broadcast(new SetOverlayItem("LOGS")));
        logsButton.setTooltip(new Tooltip(i18n().tr("Open application logs")));

        this.getItems().addAll(status, spacer, logsButton);
    }
}
