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
import atlantafx.base.theme.Tweaks;
import jakarta.inject.Inject;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Window;
import ooo.autopo.app.ConstrainedObservableList;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.injector.Auto;

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
@Auto
public class LogListView extends ListView<LogMessage> {

    @Inject
    public LogListView() {
        var items = new ConstrainedObservableList<LogMessage>(app().persistentSettings().get(LOGVIEW_ROWS_NUMBER));
        app().persistentSettings().settingsChanges(LOGVIEW_ROWS_NUMBER).subscribe((o, n) -> {
            n.ifPresent(items::setMaxCapacity);
        });
        setId("log-view");
        getStyleClass().addAll(Styles.DENSE, Styles.STRIPED, Tweaks.EDGE_TO_EDGE);
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
