package ooo.autopo.app.ui.editor;

/*
 * This file is part of the Autopo project
 * Created 25/02/25
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

import jakarta.inject.Inject;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.util.Subscription;
import ooo.autopo.model.LoadingStatus;
import ooo.autopo.model.ui.SetStatusLabelRequest;
import ooo.autopo.model.ui.SetTitleRequest;
import ooo.autopo.model.ui.TranslationsCountChanged;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ooo.autopo.app.context.ApplicationContext.app;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
public class TranslationsPane extends BorderPane {
    @Inject
    public TranslationsPane(TranslationsTableToolbar translationsTableToolbar, TranslationsTable translationsTable) {
        setTop(translationsTableToolbar);
        setCenter(translationsTable);

        app().runtimeState().poFile().subscribe(poFile -> {
            if (nonNull(poFile)) {
                final Subscription[] subscription = new Subscription[1];
                subscription[0] = poFile.status().subscribe(status -> {
                    if (status == LoadingStatus.LOADED) {
                        Platform.runLater(() -> {
                            translationsTable.setItemsAndSort(poFile.entries());
                            eventStudio().broadcast(TranslationsCountChanged.INSTANCE);
                            eventStudio().broadcast(SetTitleRequest.SET_DEFAULT_TITLE_REQUEST);
                            eventStudio().broadcast(new SetStatusLabelRequest(poFile.poFile().getFileName().toString()));
                            poFile.modifiedProperty().subscribe(modified -> eventStudio().broadcast(SetTitleRequest.SET_DEFAULT_TITLE_REQUEST));
                        });
                        ofNullable(subscription[0]).ifPresent(Subscription::unsubscribe);
                    }
                    if (status == LoadingStatus.LOADED || status == LoadingStatus.ERROR) {
                        ofNullable(subscription[0]).ifPresent(Subscription::unsubscribe);
                    }
                });
            } else {
                translationsTable.setItems(null);
            }
        });

    }
}
