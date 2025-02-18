package ooo.autopo.app.ui.logs;
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

import jakarta.inject.Inject;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Window;
import ooo.autopo.app.ConstrainedObservableList;
import org.pdfsam.eventstudio.annotation.EventListener;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ooo.autopo.app.context.ApplicationContext.app;
import static ooo.autopo.app.context.IntegerPersistentProperty.LOGVIEW_ROWS_NUMBER;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * {@link ListView} showing log messages
 *
 * @author Andrea Vacondio
 */
class LogListView extends ListView<LogMessage> {

    @Inject
    public LogListView() {
        var items = new ConstrainedObservableList<LogMessage>(app().persistentSettings().get(LOGVIEW_ROWS_NUMBER));
        app().persistentSettings().settingsChanges(LOGVIEW_ROWS_NUMBER).subscribe((o, n) -> {
            n.ifPresent(items::setMaxCapacity);
        });
        setId("log-view");
        setItems(items);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setCellFactory(list -> new TextCell());
        eventStudio().addAnnotatedListeners(this);
    }

    static class TextCell extends ListCell<LogMessage> {
        @Override
        public void updateItem(LogMessage item, boolean empty) {
            super.updateItem(item, empty);
            for (LogLevel current : LogLevel.values()) {
                getStyleClass().remove(current.style());
            }
            if (nonNull(item)) {
                setText(item.message());
                getStyleClass().add(item.level().style());
            } else {
                setText("");
            }
        }
    }

    @EventListener
    public void onEvent(LogMessage event) {
        Platform.runLater(() -> {
            getItems().add(event);
            scrollToBottomIfShowing();
        });
    }

    public void scrollToBottomIfShowing() {
        if (!getItems().isEmpty() && ofNullable(this.getScene()).map(Scene::getWindow).map(Window::isShowing).orElse(Boolean.TRUE)) {
            scrollTo(getItems().size() - 1);
        }
    }
}
