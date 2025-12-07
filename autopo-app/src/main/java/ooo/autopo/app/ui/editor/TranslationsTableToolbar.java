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

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.theme.Styles;
import jakarta.inject.Inject;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import ooo.autopo.app.DebouncedStringProperty;
import ooo.autopo.app.context.IntegerPersistentProperty;
import javafx.util.Subscription;
import ooo.autopo.model.LoadingStatus;
import ooo.autopo.model.ai.AIModelDescriptor;
import ooo.autopo.model.ai.TranslationRequest;
import ooo.autopo.model.po.PoFile;
import ooo.autopo.model.po.PoLoadRequest;
import ooo.autopo.model.po.PoSaveRequest;
import ooo.autopo.model.po.PoUpdateRequest;
import ooo.autopo.model.ui.SearchTranslationRequest;
import ooo.autopo.model.ui.TranslationsCountChanged;
import org.kordamp.ikonli.fluentui.FluentUiRegularAL;
import org.kordamp.ikonli.fluentui.FluentUiRegularMZ;
import org.kordamp.ikonli.javafx.FontIcon;
import org.pdfsam.eventstudio.annotation.EventListener;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static javafx.beans.binding.Bindings.and;
import static javafx.beans.binding.Bindings.createBooleanBinding;
import static javafx.beans.binding.Bindings.isNotNull;
import static javafx.beans.binding.Bindings.not;
import static javafx.beans.binding.Bindings.notEqual;
import static ooo.autopo.app.context.ApplicationContext.app;
import static ooo.autopo.i18n.I18nContext.i18n;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
public class TranslationsTableToolbar extends HBox {

    private final Label status = new Label();
    private final DebouncedStringProperty filterProperty = new DebouncedStringProperty();

    @Inject
    public TranslationsTableToolbar() {
        this.getStyleClass().addAll("tool-bar", "translations-toolbar");
        var saveButton = new TranslationsTableToolbar.SaveButton();
        app().runtimeState().poFile().subscribe(poFile -> {
            saveButton.disableProperty().unbind();
            saveButton.setDisable(isNull(poFile));
            if (nonNull(poFile)) {
                saveButton.disableProperty().bind(not(poFile.modifiedProperty()));
            }
        });

        var updateButton = new TranslationsTableToolbar.UpdateButton();
        app().runtimeState().project().subscribe(project -> {
            if (nonNull(project)) {
                project.pot()
                       .subscribe((o, n) -> updateButton.disableProperty()
                                                        .bind(and(isNotNull(app().runtimeState().poFile()), notEqual(n.status(), LoadingStatus.LOADED))));
            }
        });

        var batchTranslateButton = new TranslationsTableToolbar.TranslateButton();
        app().runtimeState().poFile().subscribe(poFile -> {
            batchTranslateButton.disableProperty().unbind();
            batchTranslateButton.setDisable(true);
            if (nonNull(poFile)) {
                batchTranslateButton.disableProperty().bind(notEqual(poFile.status(), LoadingStatus.LOADED));
            }
        });

        var batchTranslateAllButton = new TranslationsTableToolbar.TranslateAllFilesButton();
        app().runtimeState().project().subscribe(project -> {
            batchTranslateAllButton.disableProperty().unbind();
            batchTranslateAllButton.setDisable(isNull(project));
        });

        var search = new HBox();
        search.setAlignment(Pos.CENTER);
        HBox.setHgrow(search, Priority.ALWAYS);
        var searchField = new CustomTextField();
        searchField.getStyleClass().addAll("search-field");
        searchField.setLeft(new FontIcon(FluentUiRegularMZ.SEARCH_20));
        searchField.setPrefWidth(400);
        searchField.setPromptText(i18n().tr("Search"));
        searchField.getStyleClass().addAll(Styles.SMALL);
        searchField.textProperty().subscribe(filterProperty::set);
        searchField.setOnAction(e -> eventStudio().broadcast(new SearchTranslationRequest(searchField.getText())));
        filterProperty.subscribe((o, n) -> eventStudio().broadcast(new SearchTranslationRequest(n)));

        var searchDismiss = new Button();
        searchDismiss.getStyleClass().add("search-dismiss");
        searchDismiss.setGraphic(new FontIcon(FluentUiRegularAL.DISMISS_16));
        searchDismiss.getStyleClass().addAll(Styles.SMALL, Styles.FLAT);
        searchDismiss.setFocusTraversable(true);
        searchDismiss.setCursor(Cursor.DEFAULT);
        searchDismiss.visibleProperty().bind(createBooleanBinding(() -> isNotBlank(searchField.textProperty().get()), searchField.textProperty()));
        searchDismiss.setOnAction(e -> searchField.setText(""));
        searchField.setRight(searchDismiss);
        search.getChildren().addAll(searchField);
        app().runtimeState().poFile().subscribe(poFile -> {
            if (isNull(poFile)) {
                status.setText("");
                searchField.setText("");
            }
        });

        getChildren().addAll(saveButton, updateButton, batchTranslateButton, batchTranslateAllButton, search, status);
        eventStudio().addAnnotatedListeners(this);
    }

    @EventListener
    public void onTranslationsChanged(TranslationsCountChanged e) {
        var text = ofNullable(app().currentPoFile()).map(PoFile::entries).map(po -> {
            int entries = 0;
            int translated = 0;
            for (var entry : po) {
                entries++;
                if (isNotBlank(entry.translatedValue().get())) {
                    translated++;
                }
            }
            return i18n().tr("Translated {0} out of {1}", Integer.toString(translated), Integer.toString(entries));

        }).orElse("");
        Platform.runLater(() -> status.setText(text));
    }

    static class SaveButton extends Button {
        public SaveButton() {
            setText(i18n().tr("_Save"));
            setGraphic(new FontIcon(FluentUiRegularMZ.SAVE_20));
            getStyleClass().addAll(Styles.SMALL);
            setDefaultButton(true);
            setOnAction(e -> eventStudio().broadcast(new PoSaveRequest(app().currentPoFile())));
        }
    }

    static class UpdateButton extends Button {
        public UpdateButton() {
            setText(i18n().tr("_Update from pot"));
            setGraphic(new FontIcon(FluentUiRegularAL.ARROW_SYNC_24));
            getStyleClass().addAll(Styles.SMALL);
            setDisable(true);
            setOnAction(e -> eventStudio().broadcast(new PoUpdateRequest(app().currentProject().pot().get(), app().currentPoFile())));
        }
    }

    static class TranslateButton extends Button {
        public TranslateButton() {
            setText(i18n().tr("_Batch AI translation"));
            setTooltip(new Tooltip(i18n().tr("Translate a batch of untranslated entries with the selected AI model (The batch size is defined in the settings)")));
            setGraphic(new FontIcon(FluentUiRegularAL.BOT_24));
            getStyleClass().addAll(Styles.SMALL);
            setDisable(true);
            setOnAction(new AIActionEventHandler() {
                @Override
                void onPositiveAction(AIModelDescriptor aiModelDescriptor, String description) {
                    eventStudio().broadcast(new TranslationRequest(app().currentPoFile(),
                                                                   app().currentPoFile()
                                                                        .entries()
                                                                        .stream()
                                                                        .filter(e -> isBlank(e.translatedValue().get()))
                                                                        .limit(app().persistentSettings().get(IntegerPersistentProperty.BATCH_SIZE))
                                                                        .toList(),
                                                                   aiModelDescriptor,
                                                                   description));
                }
            });
        }

    }

    static class TranslateAllFilesButton extends Button {
        public TranslateAllFilesButton() {
            setText(i18n().tr("_Batch AI translation (All Files)"));
            setTooltip(new Tooltip(i18n().tr("Translate a batch of untranslated entries in all project files with the selected AI model (The batch size is defined in the settings)")));
            setGraphic(new FontIcon(FluentUiRegularAL.BOT_24));
            getStyleClass().addAll(Styles.SMALL);
            setDisable(true);
            setOnAction(new AIActionEventHandler() {
                @Override
                void onPositiveAction(AIModelDescriptor aiModelDescriptor, String description) {
                    var project = app().currentProject();
                    if (nonNull(project)) {
                        var files = project.translations().stream().toList();
                        processNextFile(files.iterator(), aiModelDescriptor, description);
                    }
                }
            });
        }

        private void processNextFile(java.util.Iterator<PoFile> iterator, AIModelDescriptor aiModelDescriptor, String description) {
            if (!iterator.hasNext()) {
                return;
            }

            var poFile = iterator.next();
            app().runtimeState().poFile(poFile);

            if (poFile.status().getValue() == LoadingStatus.LOADED) {
                translateCurrentFile(aiModelDescriptor, description, iterator);
            } else {
                // Load file first - PoLoadController will automatically load it when we set it as current
                final Subscription[] subscription = new Subscription[1];
                subscription[0] = poFile.status().subscribe(status -> {
                    if (status == LoadingStatus.LOADED) {
                        ofNullable(subscription[0]).ifPresent(Subscription::unsubscribe);
                        translateCurrentFile(aiModelDescriptor, description, iterator);
                    } else if (status == LoadingStatus.ERROR) {
                        ofNullable(subscription[0]).ifPresent(Subscription::unsubscribe);
                        // Skip this file and continue with next
                        processNextFile(iterator, aiModelDescriptor, description);
                    }
                });
            }
        }

        private void translateCurrentFile(AIModelDescriptor aiModelDescriptor, String description, java.util.Iterator<PoFile> iterator) {
            var currentPoFile = app().currentPoFile();
            if (isNull(currentPoFile) || currentPoFile.status().getValue() != LoadingStatus.LOADED) {
                // File not ready, skip to next
                processNextFile(iterator, aiModelDescriptor, description);
                return;
            }

            var untranslatedEntries = currentPoFile.entries()
                    .stream()
                    .filter(e -> isBlank(e.translatedValue().get()))
                    .limit(app().persistentSettings().get(IntegerPersistentProperty.BATCH_SIZE))
                    .toList();

            if (untranslatedEntries.isEmpty()) {
                // No untranslated entries, move to next file
                processNextFile(iterator, aiModelDescriptor, description);
                return;
            }

            // Use exactly the same code as TranslateButton
            var request = new TranslationRequest(currentPoFile, untranslatedEntries, aiModelDescriptor, description);
            eventStudio().broadcast(request);
            
            final Subscription[] subscription = new Subscription[1];
            subscription[0] = request.complete().subscribe((o, n) -> {
                if (n) {
                    ofNullable(subscription[0]).ifPresent(Subscription::unsubscribe);
                    // Move to next file after translation completes
                    processNextFile(iterator, aiModelDescriptor, description);
                }
            });
        }
    }
}
