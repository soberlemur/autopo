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
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import ooo.autopo.model.ai.AIModelDescriptor;
import ooo.autopo.model.ai.AssessmentRequest;
import ooo.autopo.model.ai.TranslationAssessment;
import ooo.autopo.model.ai.TranslationRequest;
import ooo.autopo.model.po.PoEntry;
import ooo.autopo.model.po.PoUpdateRequest;
import ooo.autopo.model.ui.SearchTranslation;
import org.kordamp.ikonli.fluentui.FluentUiRegularMZ;
import org.kordamp.ikonli.javafx.FontIcon;
import org.pdfsam.eventstudio.annotation.EventListener;

import java.util.Optional;

import static java.util.Comparator.comparingInt;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;
import static ooo.autopo.app.context.ApplicationContext.app;
import static ooo.autopo.i18n.I18nContext.i18n;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
public class TranslationsTable extends TableView<PoEntry> {

    TableColumn<PoEntry, String> translationColumn = new TableColumn<>(i18n().tr("Translation"));

    @Inject
    public TranslationsTable() {
        this.setEditable(false);
        getStyleClass().add("translations-table");
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        getStyleClass().addAll(Styles.DENSE, Styles.STRIPED, Tweaks.EDGE_TO_EDGE);
        var warningColumn = new TableColumn<PoEntry, ObservableList<String>>();
        warningColumn.setCellValueFactory(param -> new SimpleListProperty<>(param.getValue().warnings()));
        warningColumn.setMaxWidth(30);
        warningColumn.setMinWidth(30);
        // Custom cell factory to set background color based on the warning status
        warningColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(ObservableList<String> item, boolean empty) {
                super.updateItem(item, empty);
                setText(null);
                if (empty || item == null || item.isEmpty()) {
                    setGraphic(null);
                    setTooltip(null);
                } else {
                    var icon = new FontIcon(FluentUiRegularMZ.WARNING_20);
                    icon.getStyleClass().add(Styles.WARNING);
                    setGraphic(icon);
                    setTooltip(new Tooltip(String.join("\n", item)));
                }
            }
        });

        var sourceColumn = new TableColumn<PoEntry, String>(i18n().tr("Source"));
        sourceColumn.setPrefWidth(250);
        sourceColumn.setComparator(naturalOrder());
        sourceColumn.setCellValueFactory(param -> param.getValue().untranslatedValue());

        translationColumn.setCellValueFactory(param -> param.getValue().translatedValue());

        var assessmentColumn = new TableColumn<PoEntry, TranslationAssessment>(i18n().tr("Rate"));
        assessmentColumn.setPrefWidth(25);
        assessmentColumn.setComparator(nullsLast(comparingInt(TranslationAssessment::score)));
        assessmentColumn.setCellValueFactory(param -> param.getValue().assessment());
        assessmentColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(TranslationAssessment item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(Integer.toString(item.score()));
                }
            }
        });

        getSelectionModel().selectedItemProperty().subscribe((oldValue, newValue) -> app().runtimeState().poEntry(newValue));

        getColumns().addAll(warningColumn, sourceColumn, translationColumn, assessmentColumn);
        getSortOrder().add(translationColumn);

        var contextMenu = new ContextMenu();
        var clearMenuItem = new MenuItem(i18n().tr("Clear selected translations"));
        clearMenuItem.disableProperty().bind(getSelectionModel().selectedItemProperty().isNull());
        clearMenuItem.setOnAction(e -> getSelectionModel().getSelectedItems().forEach(po -> po.translatedValue().set("")));

        var translateMenuItem = new MenuItem(i18n().tr("Translate selected with AI"));
        translateMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.SHORTCUT_DOWN, KeyCombination.ALT_DOWN));
        translateMenuItem.disableProperty().bind(getSelectionModel().selectedItemProperty().isNull());
        translateMenuItem.setOnAction(new AIActionEventHandler() {
            @Override
            void onPositiveAction(AIModelDescriptor aiModelDescriptor, String description) {
                eventStudio().broadcast(new TranslationRequest(app().currentPoFile(), getSelectionModel().getSelectedItems(), aiModelDescriptor, description));
            }
        });

        var validateMenuItem = new MenuItem(i18n().tr("Validate selected with AI"));
        validateMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.SHORTCUT_DOWN, KeyCombination.ALT_DOWN));
        validateMenuItem.disableProperty().bind(getSelectionModel().selectedItemProperty().isNull());
        validateMenuItem.setOnAction(new AIActionEventHandler() {
            @Override
            void onPositiveAction(AIModelDescriptor aiModelDescriptor, String description) {
                eventStudio().broadcast(new AssessmentRequest(app().currentPoFile(), getSelectionModel().getSelectedItems(), aiModelDescriptor, description));
            }

            @Override
            Optional<AIModelDescriptor> getModel() {
                return app().validationAIModelDescriptor();
            }
        });

        contextMenu.getItems().addAll(clearMenuItem, translateMenuItem, validateMenuItem);
        setContextMenu(contextMenu);

        eventStudio().addAnnotatedListeners(this);
    }

    void setItemsAndSort(ObservableList<PoEntry> items) {
        setItems(items);
        getSortOrder().add(translationColumn);
        translationColumn.setSortType(TableColumn.SortType.ASCENDING);
        sort();
    }

    @EventListener
    public void onSearch(SearchTranslation text) {
        if (isNotBlank(text.needle())) {
            var start = getSelectionModel().getSelectedIndex() + 1;
            for (int i = start; i < getItems().size() + start; i++) {
                var index = i % getItems().size();
                var item = getItems().get(index);
                if (item.contains(text.needle())) {
                    getSelectionModel().select(index);
                    scrollTo(item);
                    break;
                }
            }
        }
    }

    @EventListener(priority = Integer.MIN_VALUE)
    public void onPoUpdate(PoUpdateRequest request) {
        getSelectionModel().clearSelection();
        app().runtimeState().poEntry(null);
    }
}
