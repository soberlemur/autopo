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

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import ooo.autopo.model.ui.SetOverlayItem;
import org.kordamp.ikonli.fluentui.FluentUiRegularAL;
import org.kordamp.ikonli.javafx.FontIcon;

import static ooo.autopo.i18n.I18nContext.i18n;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
public class FooterBar extends HBox {

    private final Label status = new Label();

    public FooterBar() {
        this.getStyleClass().add("footer-pane");
        this.status.getStyleClass().add("status-label");
        this.status.setText("Ready");
        HBox buttonGroup = new HBox();
        buttonGroup.getStyleClass().add("footer-buttons");

        var logsButton = new Button();
        logsButton.getStyleClass().add("footer-button");
        logsButton.setGraphic(new FontIcon(FluentUiRegularAL.DOCUMENT_ONE_PAGE_24));
        logsButton.setOnAction(e -> eventStudio().broadcast(new SetOverlayItem("LOGS")));
        logsButton.setTooltip(new Tooltip(i18n().tr("Open application logs")));
        buttonGroup.getChildren().add(logsButton);
        HBox.setHgrow(buttonGroup, javafx.scene.layout.Priority.ALWAYS);
        this.getChildren().addAll(status, buttonGroup);
    }
}
