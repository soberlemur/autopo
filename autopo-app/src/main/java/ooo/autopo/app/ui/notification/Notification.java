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

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import ooo.autopo.app.ui.Style;
import ooo.autopo.model.notification.RemoveNotificationRequest;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.UUID;

import static org.pdfsam.eventstudio.StaticStudio.eventStudio;
import static org.sejda.commons.util.RequireUtils.requireNotNullArg;

/**
 * Container for a basic notification with title, text and icon.
 *
 * @author Andrea Vacondio
 */
class Notification extends VBox {

    private final FadeTransition fade = new FadeTransition(Duration.millis(500), this);

    Notification(String title, Node content) {
        requireNotNullArg(content, "Notification content cannot be blank");
        getStyleClass().add("notification");
        getStyleClass().addAll(Style.CONTAINER.css());
        setId(UUID.randomUUID().toString());
        var closeButton = new Button();
        closeButton.setGraphic(new FontIcon(FluentUiFilledAL.DISMISS_20));
        closeButton.getStyleClass().addAll(Style.TOOLBAR_BUTTON.css());
        closeButton.getStyleClass().add("big");
        closeButton.setOnAction(e -> eventStudio().broadcast(new RemoveNotificationRequest(getId())));
        var titleLabel = new Label(title);
        titleLabel.setPrefWidth(Integer.MAX_VALUE);
        titleLabel.getStyleClass().add("notification-title");
        var top = new StackPane(titleLabel, closeButton);
        top.setAlignment(Pos.CENTER_RIGHT);
        getChildren().addAll(top, content);
        setOpacity(0);
        setOnMouseEntered(e -> {
            fade.stop();
            setOpacity(1);
        });
        setOnMouseClicked(e -> {
            setOnMouseEntered(null);
            setOnMouseExited(null);
            fade.stop();
            eventStudio().broadcast(new RemoveNotificationRequest(getId()));
        });
        fade.setFromValue(1);
        fade.setToValue(0);
    }

    void onFade(EventHandler<ActionEvent> onFaded) {
        fade.setOnFinished(onFaded);
    }

    void fadeAway(Duration delay) {
        fade.stop();
        fade.setDelay(delay);
        fade.jumpTo(Duration.ZERO);
        fade.play();
    }

    void fadeAway() {
        fadeAway(Duration.ZERO);
    }
}
