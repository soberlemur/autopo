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
import jakarta.inject.Inject;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Subscription;
import ooo.autopo.app.ui.Style;
import ooo.autopo.model.ai.TranslationRequest;
import ooo.autopo.model.notification.AddNotificationRequest;
import ooo.autopo.model.notification.NotificationType;
import ooo.autopo.model.project.ProjectProperty;

import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static ooo.autopo.app.context.ApplicationContext.app;
import static ooo.autopo.i18n.I18nContext.i18n;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
public class TranslateEntryPanel extends SplitPane {

    private Subscription entryModifiedSubscription;

    @Inject
    public TranslateEntryPanel() {
        this.setOrientation(Orientation.HORIZONTAL);
        this.setDividerPositions(0.90);

        var entriesPanel = new VBox();
        var sourceView = new TextArea();
        sourceView.getStyleClass().add(Tweaks.EDGE_TO_EDGE);
        sourceView.setWrapText(true);
        sourceView.setEditable(false);
        VBox.setVgrow(sourceView, Priority.ALWAYS);

        var toolbar = new HBox();
        toolbar.getStyleClass().addAll("tool-bar", "translation-edit-toolbar");
        var aiTranslateButton = new Button(i18n().tr("AI Translate"));
        aiTranslateButton.getStyleClass().addAll(Styles.SMALL);
        app().runtimeState().poEntry().subscribe(e -> aiTranslateButton.setDisable(isNull(e) || isNull(app().currentPoFile().locale())));

        aiTranslateButton.setOnAction(e -> {
            if (isBlank(app().currentProject().getProperty(ProjectProperty.DESCRIPTION))) {
                eventStudio().broadcast(new AddNotificationRequest(NotificationType.WARN,
                                                                   i18n().tr(
                                                                           "Add a project description to give the AI model more context and improve translations accuracy")));
            }
            if (isNull(app().currentPoFile().locale())) {
                eventStudio().broadcast(new AddNotificationRequest(NotificationType.ERROR, i18n().tr("The project must have a target locale to translate to")));
            } else {
                eventStudio().broadcast(new TranslationRequest(app().currentPoFile(),
                                                               app().currentPoEntry(),
                                                               app().currentAIModelDescriptor().get(),
                                                               app().currentProject().getProperty(ProjectProperty.DESCRIPTION)));
            }
        });

        var aiValidateButton = new Button(i18n().tr("AI Validate"));
        aiValidateButton.getStyleClass().addAll(Styles.SMALL);
        aiValidateButton.disableProperty().bind(aiTranslateButton.disableProperty());

        toolbar.getChildren().addAll(aiTranslateButton, aiValidateButton);
        var translationView = new TextArea();
        translationView.getStyleClass().add(Tweaks.EDGE_TO_EDGE);
        translationView.setWrapText(true);
        translationView.setEditable(false);
        VBox.setVgrow(translationView, Priority.ALWAYS);

        entriesPanel.getChildren().addAll(sourceView, toolbar, translationView);

        var commentsFlow = new VBox();
        commentsFlow.getStyleClass().addAll(Style.CONTAINER.css());
        var commentsScroll = new ScrollPane(commentsFlow);
        commentsScroll.setFitToWidth(true);
        commentsScroll.setFitToHeight(true);

        this.getItems().addAll(entriesPanel, commentsScroll);

        app().runtimeState().poEntry().subscribe((oldEntry, newEntry) -> {
            //let's remove the old binding
            ofNullable(oldEntry).ifPresent(poEntry -> poEntry.translatedValue().unbindBidirectional(translationView.textProperty()));
            ofNullable(entryModifiedSubscription).ifPresent(Subscription::unsubscribe);
            sourceView.setText(ofNullable(newEntry).map(entry -> entry.untranslatedValue().getValue()).orElse(""));
            translationView.setText(ofNullable(newEntry).map(entry -> entry.translatedValue().getValue()).orElse(""));
            translationView.setEditable(false);
            commentsFlow.getChildren().clear();
            if (Objects.nonNull(newEntry)) {
                translationView.setEditable(true);
                newEntry.translatedValue().bindBidirectional(translationView.textProperty());
                entryModifiedSubscription = translationView.textProperty().subscribe((o, n) -> app().currentPoFile().modified(true));

                commentsFlow.getChildren().add(createSection(i18n().tr("Comments"), newEntry.comments()));
                commentsFlow.getChildren().add(createSection(i18n().tr("Extracted comments"), newEntry.comments()));
                commentsFlow.getChildren().add(createSection(i18n().tr("Formats"), newEntry.formats()));
                commentsFlow.getChildren().add(createSection(i18n().tr("Source reference"), newEntry.comments()));
            }
        });
    }

    private static TextFlow createSection(String title, Iterable<String> items) {
        var section = new TextFlow();

        var titleText = new Text(title + "\n");
        titleText.getStyleClass().add(Styles.TEXT_CAPTION);
        section.getChildren().add(titleText);

        for (String item : items) {
            Text itemText = new Text(item + "\n");
            section.getChildren().add(itemText);
        }

        return section;
    }
}
