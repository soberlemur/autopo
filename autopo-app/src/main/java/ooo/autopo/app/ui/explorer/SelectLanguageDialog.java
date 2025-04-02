package ooo.autopo.app.ui.explorer;

/*
 * This file is part of the Autopo project
 * Created 24/03/25
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
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ooo.autopo.app.DebouncedStringProperty;
import ooo.autopo.model.po.PoAddRequest;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.fluentui.FluentUiRegularAL;
import org.kordamp.ikonli.fluentui.FluentUiRegularMZ;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Locale;

import static java.util.Objects.nonNull;
import static javafx.beans.binding.Bindings.createBooleanBinding;
import static ooo.autopo.i18n.I18nContext.i18n;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
public class SelectLanguageDialog extends VBox {

    private PoAddRequest request;
    private final DebouncedStringProperty filterProperty = new DebouncedStringProperty();
    private ListView<Locale> localesListView = new ListView<>();

    public SelectLanguageDialog(Runnable onCancel) {
        getStyleClass().add("autopo-dialog");
        setSpacing(10);
        setPadding(new Insets(10));
        setAlignment(Pos.CENTER);
        setMinSize(350, 200);
        setMaxSize(450, 600);
        var label = new Label(i18n().tr("Select the language for the translation"));
        label.getStyleClass().add(Styles.TITLE_3);

        var searchField = new CustomTextField();
        searchField.getStyleClass().addAll("search-field");
        searchField.setLeft(new FontIcon(FluentUiRegularMZ.SEARCH_20));
        searchField.setPrefWidth(400);
        searchField.setPromptText(i18n().tr("Search"));
        searchField.getStyleClass().addAll(Styles.SMALL);
        searchField.textProperty().subscribe(filterProperty::set);
        searchField.setOnAction(e -> search(searchField.getText()));
        filterProperty.subscribe((o, n) -> search(n));

        var searchDismiss = new Button();
        searchDismiss.getStyleClass().add("search-dismiss");
        searchDismiss.setGraphic(new FontIcon(FluentUiRegularAL.DISMISS_16));
        searchDismiss.getStyleClass().addAll(Styles.SMALL, Styles.FLAT);
        searchDismiss.setFocusTraversable(true);
        searchDismiss.setCursor(Cursor.DEFAULT);
        searchDismiss.visibleProperty().bind(createBooleanBinding(() -> isNotBlank(searchField.textProperty().get()), searchField.textProperty()));
        searchDismiss.setOnAction(e -> searchField.setText(""));
        searchField.setRight(searchDismiss);

        var locales = FXCollections.observableArrayList(Locale.availableLocales()
                                                              .filter(l -> !l.getDisplayName().isBlank())
                                                              .sorted((a, b) -> a.getDisplayName().compareToIgnoreCase(b.getDisplayName()))
                                                              .toList());
        localesListView.getItems().setAll(locales);
        localesListView.getStyleClass().addAll(Styles.DENSE, Styles.BORDERED);
        localesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        localesListView.setPrefWidth(Integer.MAX_VALUE);
        localesListView.setPrefHeight(Integer.MAX_VALUE);
        localesListView.setCellFactory(p -> new ListCell<>() {
            @Override
            protected void updateItem(Locale item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText("");
                } else {
                    setText(StringUtils.capitalize(item.getDisplayName()) + " - " + item.toLanguageTag());
                }
            }
        });

        localesListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                selectLocaleAndBroadcast(onCancel);
            }
        });
        var cancelButton = new Button(i18n().tr("Cancel"));
        cancelButton.setOnAction(e -> onCancel.run());
        var okButton = new Button(i18n().tr("Add"));
        okButton.setDefaultButton(true);
        okButton.disableProperty().bind(localesListView.getSelectionModel().selectedItemProperty().isNull());

        okButton.setOnAction(e -> {
            selectLocaleAndBroadcast(onCancel);
        });
        var buttons = new HBox(10, cancelButton, okButton);
        buttons.setAlignment(Pos.CENTER);
        getChildren().addAll(label, searchField, localesListView, buttons);
    }

    private void selectLocaleAndBroadcast(Runnable onCancel) {
        var selected = localesListView.getSelectionModel().getSelectedItem();
        if (nonNull(selected) && nonNull(request)) {
            request.poFile().locale(selected);
            onCancel.run();
            eventStudio().broadcast(request);
        }
    }

    public void currentRequest(PoAddRequest request) {
        this.request = request;
    }

    private void search(String text) {
        if (isNotBlank(text)) {
            var start = localesListView.getSelectionModel().getSelectedIndex() + 1;
            var localesSize = localesListView.getItems().size();
            for (int i = start; i < localesSize + start; i++) {
                var index = i % localesSize;
                Locale item = localesListView.getItems().get(index);
                if (item.getDisplayName().contains(text)) {
                    localesListView.getSelectionModel().select(index);
                    localesListView.scrollTo(item);
                    break;
                }
            }
        }
    }
}
