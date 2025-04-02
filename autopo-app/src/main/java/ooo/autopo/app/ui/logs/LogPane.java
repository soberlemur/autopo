package ooo.autopo.app.ui.logs;
/*
 * This file is part of the Autopo project
 * Created 13/02/25
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
import ooo.autopo.app.ui.components.NotClosable;
import ooo.autopo.model.io.FileType;
import ooo.autopo.model.ui.log.ClearLogRequest;
import ooo.autopo.model.ui.log.SaveLogRequest;
import org.pdfsam.eventstudio.annotation.EventListener;

import java.util.Collection;
import java.util.Objects;

import static ooo.autopo.app.io.ObjectCollectionWriter.writeContent;
import static ooo.autopo.i18n.I18nContext.i18n;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * Panel displaying log messages
 *
 * @author Andrea Vacondio
 */

public class LogPane extends BorderPane implements NotClosable {

    private final LogListView logView;

    @Inject
    public LogPane(LogListView view, LogPaneToolbar toolbar) {
        this.logView = view;
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
