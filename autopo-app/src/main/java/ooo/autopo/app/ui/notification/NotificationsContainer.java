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
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import static java.util.Objects.nonNull;

/**
 * @author Andrea Vacondio
 */
public class NotificationsContainer extends VBox {

    public NotificationsContainer() {
        getStyleClass().add("notifications");
        setMaxHeight(Region.USE_PREF_SIZE);
    }

    void addNotification(String title, Node message) {
        Platform.runLater(() -> {
            Notification toAdd = doAddNotification(title, message);
            fadeIn(toAdd, e -> toAdd.fadeAway(Duration.seconds(5)));
        });
    }

    void addStickyNotification(String title, Node message) {
        Platform.runLater(() -> {
            Notification toAdd = doAddNotification(title, message);
            fadeIn(toAdd, null);
        });
    }

    private Notification doAddNotification(String title, Node message) {
        var toAdd = new Notification(title, message);
        toAdd.onFade(e -> getChildren().remove(toAdd));
        getChildren().add(toAdd);
        return toAdd;
    }

    private void fadeIn(Notification toAdd, EventHandler<ActionEvent> onFinished) {
        var transition = new FadeTransition(Duration.millis(300), toAdd);
        transition.setFromValue(0);
        transition.setToValue(1);
        if (nonNull(onFinished)) {
            transition.setOnFinished(onFinished);
        }
        transition.play();
    }

    void removeNotification(String id) {
        Node found = lookup(String.format("#%s", id));
        if (found instanceof Notification remove) {
            remove.fadeAway();
        }
    }
}
