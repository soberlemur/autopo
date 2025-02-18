package ooo.autopo.app.ui.logs;
/*
 * This file is part of the Autopo project
 * Created 13/02/25
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
import javafx.beans.binding.Bindings;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import ooo.autopo.app.io.Choosers;
import ooo.autopo.model.io.FileType;
import ooo.autopo.model.ui.log.ClearLogRequest;
import ooo.autopo.model.ui.log.SaveLogRequest;
import org.pdfsam.eventstudio.annotation.EventListener;

import java.util.Collection;
import java.util.Objects;

import static ooo.autopo.i18n.I18nContext.i18n;
import static ooo.autopo.service.io.ObjectCollectionWriter.writeContent;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * Panel displaying log messages
 *
 * @author Andrea Vacondio
 */
public class LogPane extends BorderPane {

    private final LogListView logView;

    @Inject
    public LogPane(LogListView view, LogPaneToolbar toolbar) {
        this.logView = view;
        getStyleClass().addAll("logs-pane", "spaced-container");
        setCenter(this.logView);
        setTop(toolbar);
        var copyItem = new MenuItem(i18n().tr("Copy"));
        copyItem.setId("copyLogMenuItem");
        copyItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN));
        copyItem.setOnAction(e -> copyLog(logView.getSelectionModel().getSelectedItems()));

        // disable if no selection
        copyItem.disableProperty().bind(Bindings.isEmpty(logView.getSelectionModel().getSelectedItems()));

        var clearItem = new MenuItem(i18n().tr("Clear"));
        clearItem.setId("clearLogMenuItem");
        clearItem.setOnAction(e -> clearLog(null));
        // disable if there's no text
        clearItem.disableProperty().bind(Bindings.isEmpty(logView.getItems()));

        var selectAllItem = new MenuItem(i18n().tr("Select all"));
        selectAllItem.setId("selectAllLogMenuItem");
        selectAllItem.setOnAction(e -> logView.getSelectionModel().selectAll());
        // disable if there's no text
        selectAllItem.disableProperty().bind(clearItem.disableProperty());

        var saveItem = new MenuItem(i18n().tr("Save log"));
        saveItem.setId("saveLogMenuItem");
        saveItem.setOnAction(e -> saveLog(null));
        // disable if there's no text
        saveItem.disableProperty().bind(clearItem.disableProperty());
        logView.setContextMenu(new ContextMenu(copyItem, clearItem, selectAllItem, new SeparatorMenuItem(), saveItem));
        eventStudio().addAnnotatedListeners(this);
    }

    @EventListener
    public void saveLog(SaveLogRequest request) {
        var fileChooser = Choosers.fileChooser(i18n().tr("Select where to save the log file"), FileType.LOG);
        fileChooser.setInitialFileName("autopo.log");
        var saveTo = fileChooser.showSaveDialog(this.getScene().getWindow());
        if (Objects.nonNull(saveTo)) {
            //            if (chosenFile.exists()) {
            //                 TODO show dialog? investigate. On Ubuntu it already asks confirmation.
            //            }
            writeContent(logView.getItems()).to(saveTo);
        }
    }

    @EventListener
    public void clearLog(ClearLogRequest request) {
        logView.getItems().clear();
    }

    private void copyLog(Collection<LogMessage> selected) {
        if (!selected.isEmpty()) {
            var content = new ClipboardContent();
            writeContent(selected).to(content);
            Clipboard.getSystemClipboard().setContent(content);
        }
    }
}
