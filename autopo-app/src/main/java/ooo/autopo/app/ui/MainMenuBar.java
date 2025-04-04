package ooo.autopo.app.ui;

/*
 * This file is part of the Autopo project
 * Created 14/02/25
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
import javafx.application.Platform;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import ooo.autopo.app.io.Choosers;
import ooo.autopo.model.LoadingStatus;
import ooo.autopo.model.io.IOEvent;
import ooo.autopo.model.project.Project;
import ooo.autopo.model.ui.SetOverlayItemRequest;
import ooo.autopo.service.project.RecentsService;
import org.apache.commons.lang3.StringUtils;
import org.pdfsam.eventstudio.annotation.EventListener;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static javafx.beans.binding.Bindings.isNull;
import static javafx.beans.binding.Bindings.notEqual;
import static ooo.autopo.app.context.ApplicationContext.app;
import static ooo.autopo.i18n.I18nContext.i18n;
import static ooo.autopo.model.io.FileType.OOO;
import static ooo.autopo.model.io.IOEventType.LOADED;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
public class MainMenuBar extends MenuBar {

    private final Menu recent;
    private final RecentsService recentsService;

    @Inject
    public MainMenuBar(RecentsService recentsService) {
        this.recentsService = recentsService;
        var exit = new MenuItem(i18n().tr("Exit"));
        exit.setId("exitMenuItem");
        exit.setOnAction(e -> Platform.exit());

        var projects = new Menu(i18n().tr("Project"));
        projects.setId("projectsMenuItem");
        var open = new MenuItem(i18n().tr("Open"));
        open.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
        open.setOnAction(e -> {
            var translations = ofNullable(app().currentProject()).map(Project::translations).orElse(Collections.emptySortedSet());
            var hasModified = translations.stream().anyMatch(poFile -> poFile.modifiedProperty().get());
            var openChooser = true;
            if (hasModified) {
                var dialog = new DiscardModifiedPoFilesConfirmationDialog();
                dialog.initOwner(getScene().getWindow());
                openChooser = dialog.showAndWait().filter(b -> b.getButtonData() == ButtonBar.ButtonData.YES).isPresent();
            }
            if (openChooser) {
                var directoryChooser = Choosers.directoryChooser(i18n().tr("Select the project directory"));

                ofNullable(directoryChooser.showDialog(this.getScene().getWindow())).filter(Files::isDirectory)
                                                                                    .map(Project::new)
                                                                                    .ifPresent(app().runtimeState()::project);
            }
        });

        var editProject = new MenuItem(i18n().tr("Edit project"));
        editProject.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.ALT_DOWN, KeyCombination.SHIFT_DOWN));
        editProject.setOnAction(e -> eventStudio().broadcast(new SetOverlayItemRequest("PROJECT_SETTINGS")));
        editProject.disableProperty().bind(isNull(app().runtimeState().project()));

        var addTranslation = new MenuItem(i18n().tr("Add translation"));
        // addTranslation.setOnAction(e -> eventStudio().broadcast(new PoAddRequest()));
        addTranslation.disableProperty().bind(isNull(app().runtimeState().project()));
        app().runtimeState().project().subscribe(project -> {
            addTranslation.disableProperty().unbind();
            addTranslation.setDisable(true);
            if (nonNull(project)) {
                project.pot().subscribe(pot -> {
                    if (nonNull(pot)) {
                        addTranslation.disableProperty().bind(notEqual(pot.status(), LoadingStatus.LOADED));
                    }
                });
            }
        });

        recent = new Menu(i18n().tr("Recents"));
        recent.setId("recentsMenuItem");
        populateRecents();
        var clear = new MenuItem(i18n().tr("Clear recents"));
        clear.setOnAction(e -> {
            recentsService.clear();
            recent.getItems().clear();
        });
        clear.setId("clearWorkspaces");

        projects.getItems().addAll(open, editProject, addTranslation, new SeparatorMenuItem(), recent, clear);

        var fileMenu = new Menu(i18n().tr("_File"));
        fileMenu.getItems().addAll(projects, new SeparatorMenuItem(), exit);

        var settings = new MenuItem(i18n().tr("Settings"));
        settings.setId("settingsMenuItem");
        settings.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN, KeyCombination.ALT_DOWN));
        settings.setOnAction(e -> eventStudio().broadcast(new SetOverlayItemRequest("SETTINGS")));
        var editMenu = new Menu(i18n().tr("_Edit"));
        editMenu.getItems().addAll(settings);

        var about = new MenuItem(i18n().tr("About"));
        about.setId("aboutMenuItem");
        about.setOnAction(e -> eventStudio().broadcast(new SetOverlayItemRequest("ABOUT")));

        var logs = new MenuItem(i18n().tr("Logs"));
        logs.setId("logsMenuItem");
        logs.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN));
        logs.setOnAction(e -> eventStudio().broadcast(new SetOverlayItemRequest("LOGS")));

        var helpMenu = new Menu(i18n().tr("_Help"));
        helpMenu.getItems().addAll(logs, new SeparatorMenuItem(), about);
        getMenus().addAll(fileMenu, editMenu, helpMenu);
        eventStudio().addAnnotatedListeners(this);
    }

    @EventListener(priority = Integer.MIN_VALUE)
    public void onProjectLoaded(IOEvent event) {
        if (LOADED == event.type() && OOO == event.fileType()) {
            recent.getItems().clear();
            populateRecents();
        }
    }

    private void populateRecents() {
        recentsService.getRecentProjects().stream().filter(StringUtils::isNotBlank).map(this::recentProjectMenuItem).forEach(recent.getItems()::add);
    }

    MenuItem recentProjectMenuItem(String path) {
        var item = new MenuItem(StringUtils.abbreviate(path, path.length(), 60));
        item.setOnAction(a -> {
            var translations = ofNullable(app().currentProject()).map(Project::translations).orElse(Collections.emptySortedSet());
            var hasModified = translations.stream().anyMatch(poFile -> poFile.modifiedProperty().get());
            var openChooser = true;
            if (hasModified) {
                var dialog = new DiscardModifiedPoFilesConfirmationDialog();
                dialog.initOwner(getScene().getWindow());
                openChooser = dialog.showAndWait().filter(b -> b.getButtonData() == ButtonBar.ButtonData.YES).isPresent();
            }
            if (openChooser) {
                app().runtimeState().project(new Project(Paths.get(path)));
            }

        });
        item.setMnemonicParsing(false);
        return item;
    }

}
