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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import ooo.autopo.model.po.PoEntry;
import ooo.autopo.model.ui.SearchTranslation;
import org.kordamp.ikonli.fluentui.FluentUiRegularMZ;
import org.kordamp.ikonli.javafx.FontIcon;
import org.pdfsam.eventstudio.annotation.EventListener;

import java.util.Comparator;

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
        getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
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
        sourceColumn.setComparator(Comparator.naturalOrder());
        sourceColumn.setCellValueFactory(param -> param.getValue().untranslatedValue());

        translationColumn.setCellValueFactory(param -> param.getValue().translatedValue());

        getSelectionModel().selectedItemProperty().subscribe((oldValue, newValue) -> app().runtimeState().poEntry(newValue));

        getColumns().addAll(warningColumn, sourceColumn, translationColumn);
        getSortOrder().add(translationColumn);
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
}
