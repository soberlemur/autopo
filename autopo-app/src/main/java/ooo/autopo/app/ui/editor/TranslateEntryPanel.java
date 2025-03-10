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

import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Subscription;

import java.util.Objects;

import static java.util.Optional.ofNullable;
import static javafx.beans.binding.Bindings.isNull;
import static ooo.autopo.app.context.ApplicationContext.app;
import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * @author Andrea Vacondio
 */
public class TranslateEntryPanel extends VBox {

    private Subscription entryModifiedSubscription;

    public TranslateEntryPanel() {
        var sourceView = new TextArea();
        sourceView.getStyleClass().add(Tweaks.EDGE_TO_EDGE);
        sourceView.setWrapText(true);
        sourceView.setEditable(false);
        VBox.setVgrow(sourceView, Priority.ALWAYS);

        var toolbar = new HBox();
        toolbar.getStyleClass().add("translation-edit-toolbar");
        var aiTranslateButton = new Button(i18n().tr("AI Translate"));
        aiTranslateButton.getStyleClass().addAll(Styles.SMALL);
        aiTranslateButton.disableProperty().bind(isNull(app().runtimeState().poEntry()));
        var aiValidateButton = new Button(i18n().tr("AI Validate"));
        aiValidateButton.getStyleClass().addAll(Styles.SMALL);
        aiValidateButton.disableProperty().bind(isNull(app().runtimeState().poEntry()));
        toolbar.getChildren().addAll(aiTranslateButton, aiValidateButton);
        var translationView = new TextArea();
        translationView.getStyleClass().add(Tweaks.EDGE_TO_EDGE);
        translationView.setWrapText(true);
        translationView.setEditable(false);
        VBox.setVgrow(translationView, Priority.ALWAYS);

        getChildren().addAll(sourceView, toolbar, translationView);

        app().runtimeState().poEntry().subscribe((oldEntry, newEntry) -> {
            //let's remove the old binding
            ofNullable(oldEntry).ifPresent(poEntry -> poEntry.translatedValue().unbind());
            ofNullable(entryModifiedSubscription).ifPresent(Subscription::unsubscribe);
            sourceView.setText(ofNullable(newEntry).map(entry -> entry.untranslatedValue().getValue()).orElse(""));
            translationView.setText(ofNullable(newEntry).map(entry -> entry.translatedValue().getValue()).orElse(""));
            translationView.setEditable(false);
            if (Objects.nonNull(newEntry)) {
                translationView.setEditable(true);
                newEntry.translatedValue().bind(translationView.textProperty());
                entryModifiedSubscription = translationView.textProperty().subscribe((o, n) -> {
                    app().currentPoFile().modified(true);
                });
            }
        });
    }
}
