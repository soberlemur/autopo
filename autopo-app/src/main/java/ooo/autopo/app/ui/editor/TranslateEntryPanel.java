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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Subscription;
import ooo.autopo.app.ui.Style;
import ooo.autopo.model.ai.AIModelDescriptor;
import ooo.autopo.model.ai.AssessmentRequest;
import ooo.autopo.model.ai.TranslationRequest;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static ooo.autopo.app.context.ApplicationContext.app;
import static ooo.autopo.i18n.I18nContext.i18n;
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
        var aiTranslateButton = new Button();
        aiTranslateButton.setOnAction(new AIActionEventHandler() {
            @Override
            void onPositiveAction(AIModelDescriptor aiModelDescriptor, String description) {
                eventStudio().broadcast(new TranslationRequest(app().currentPoFile(), app().currentPoEntry(), aiModelDescriptor, description));
            }
        });

        aiTranslateButton.setText(i18n().tr("AI Translate"));
        aiTranslateButton.getStyleClass().addAll(Styles.SMALL);
        app().runtimeState().poEntry().subscribe(e -> aiTranslateButton.setDisable(isNull(e) || isNull(app().currentPoFile().locale())));

        var aiValidateButton = new Button();
        aiValidateButton.setOnAction(new AIActionEventHandler() {
            @Override
            void onPositiveAction(AIModelDescriptor aiModelDescriptor, String description) {
                eventStudio().broadcast(new AssessmentRequest(app().currentPoFile(), app().currentPoEntry(), aiModelDescriptor, description));
            }

            @Override
            Optional<AIModelDescriptor> getModel() {
                return app().validationAIModelDescriptor();
            }
        });
        aiValidateButton.setText(i18n().tr("AI Validate"));
        aiValidateButton.getStyleClass().addAll(Styles.SMALL);

        toolbar.getChildren().addAll(aiTranslateButton, aiValidateButton);
        var translationView = new TextArea();
        translationView.getStyleClass().add(Tweaks.EDGE_TO_EDGE);
        translationView.setWrapText(true);
        translationView.setEditable(false);
        VBox.setVgrow(translationView, Priority.ALWAYS);

        aiValidateButton.disableProperty().bind(translationView.textProperty().isEmpty());
        entriesPanel.getChildren().addAll(sourceView, toolbar, translationView);

        var commentsFlow = new VBox();
        commentsFlow.getStyleClass().addAll(Style.CONTAINER.css());
        var commentsScroll = new ScrollPane(commentsFlow);
        commentsScroll.setFitToWidth(true);
        commentsScroll.setFitToHeight(true);

        var sidePane = new TabPane();
        sidePane.getStyleClass().addAll(Style.CONTAINER.css());
        sidePane.getTabs().add(new Tab(i18n().tr("Assessment"), new AssessmentPane()));
        sidePane.getTabs().add(new Tab(i18n().tr("Comments"), commentsScroll));
        //sidePane.getStyleClass().add("settings-panel");
        sidePane.getTabs().forEach(tab -> tab.setClosable(false));

        this.getItems().addAll(entriesPanel, sidePane);

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
                commentsFlow.getChildren().add(createSection(i18n().tr("Extracted comments"), newEntry.extractedComments()));
                commentsFlow.getChildren().add(createSection(i18n().tr("Formats"), newEntry.formats()));
                commentsFlow.getChildren().add(createSection(i18n().tr("Source reference"), newEntry.sourceReferences()));
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
