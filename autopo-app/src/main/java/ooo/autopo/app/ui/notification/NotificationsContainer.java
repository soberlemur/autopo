package ooo.autopo.app.ui.notification;
/*
 * This file is part of the Autopo project
 * Created 13/02/25
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

import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import ooo.autopo.model.notification.NotificationType;
import ooo.autopo.model.ui.SetOverlayItem;

import static atlantafx.base.util.Animations.fadeIn;
import static atlantafx.base.util.Animations.fadeOut;
import static ooo.autopo.i18n.I18nContext.i18n;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
public class NotificationsContainer extends VBox {

    public NotificationsContainer() {
        getStyleClass().add("notifications");
        setMaxHeight(Region.USE_PREF_SIZE);
    }

    void addNotification(String message, NotificationType type) {
        Platform.runLater(() -> {
            fadeIn(doAddNotification(message, type), Duration.millis(300)).playFromStart();
        });
    }

    private Notification doAddNotification(String message, NotificationType type) {
        var toAdd = new Notification(message, type.getGraphic());
        toAdd.getStyleClass().add(Styles.ELEVATED_1);
        switch (type) {
        case WARN -> toAdd.getStyleClass().add(Styles.WARNING);
        case ERROR -> {
            toAdd.getStyleClass().add(Styles.DANGER);
            var openLogsButton = new Button(i18n().tr("Open logs"));
            openLogsButton.setOnAction(e -> {
                toAdd.getOnClose().handle(new Event(Event.ANY));
                eventStudio().broadcast(new SetOverlayItem("LOGS"));
            });
            toAdd.setPrimaryActions(openLogsButton);
        }
        }
        var fadeOut = fadeOut(toAdd, Duration.millis(300));
        fadeOut.setOnFinished(fe -> getChildren().remove(toAdd));
        toAdd.setOnClose(e -> fadeOut.playFromStart());
        getChildren().add(toAdd);
        return toAdd;
    }

}
