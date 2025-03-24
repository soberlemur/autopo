package ooo.autopo.app.ui;

/*
 * This file is part of the Autopo project
 * Created 14/02/25
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
import ooo.autopo.model.po.PoAddRequest;
import ooo.autopo.model.project.Project;
import ooo.autopo.model.ui.SetOverlayItem;
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
        editProject.setOnAction(e -> eventStudio().broadcast(new SetOverlayItem("PROJECT_SETTINGS")));
        editProject.disableProperty().bind(isNull(app().runtimeState().project()));

        var addTranslation = new MenuItem(i18n().tr("Add translation"));
        addTranslation.setOnAction(e -> eventStudio().broadcast(new PoAddRequest()));
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
        settings.setOnAction(e -> eventStudio().broadcast(new SetOverlayItem("SETTINGS")));
        var editMenu = new Menu(i18n().tr("_Edit"));
        editMenu.getItems().addAll(settings);

        var about = new MenuItem(i18n().tr("About"));
        about.setId("aboutMenuItem");
        about.setOnAction(e -> eventStudio().broadcast(new SetOverlayItem("ABOUT")));

        var logs = new MenuItem(i18n().tr("Logs"));
        logs.setId("logsMenuItem");
        logs.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN));
        logs.setOnAction(e -> eventStudio().broadcast(new SetOverlayItem("LOGS")));

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
