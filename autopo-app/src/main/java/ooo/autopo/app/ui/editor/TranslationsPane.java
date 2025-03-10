package ooo.autopo.app.ui.editor;

/*
 * This file is part of the Autopo project
 * Created 25/02/25
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
