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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import ooo.autopo.app.io.Choosers;
import ooo.autopo.model.project.LoadProjectRequest;
import ooo.autopo.model.project.Project;
import ooo.autopo.model.ui.SetOverlayItem;
import ooo.autopo.service.project.RecentsService;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.Optional.ofNullable;
import static ooo.autopo.i18n.I18nContext.i18n;
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
        var exit = new MenuItem(i18n().tr("E_xit"));
        exit.setId("exitMenuItem");
        exit.setOnAction(e -> Platform.exit());

        var projects = new Menu(i18n().tr("_Projects"));
        projects.setId("projectsMenuItem");
        var open = new MenuItem(i18n().tr("_Open"));
        open.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
        open.setOnAction(e -> {
            var directoryChooser = Choosers.directoryChooser(i18n().tr("Select the project directory"));

            ofNullable(directoryChooser.showDialog(this.getScene().getWindow())).filter(Files::isDirectory)
                                                                                .map(Project::new)
                                                                                .map(LoadProjectRequest::new)
                                                                                .ifPresent(eventStudio()::broadcast);
        });

        recent = new Menu(i18n().tr("Recen_ts"));
        recent.setId("recentsMenuItem");
        projects.getItems().addAll(open);

        var fileMenu = new Menu(i18n().tr("File"));
        fileMenu.getItems().addAll(projects, new SeparatorMenuItem(), exit);

        var settings = new MenuItem(i18n().tr("Settings"));
        settings.setId("settingsMenuItem");
        settings.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN, KeyCombination.ALT_DOWN));
        settings.setOnAction(e -> eventStudio().broadcast(new SetOverlayItem("SETTINGS")));
        var editMenu = new Menu(i18n().tr("Edit"));
        editMenu.getItems().addAll(settings);

        var about = new MenuItem(i18n().tr("_About"));
        about.setId("aboutMenuItem");
        about.setOnAction(e -> eventStudio().broadcast(new SetOverlayItem("ABOUT")));

        var logs = new MenuItem(i18n().tr("_Logs"));
        logs.setId("logsMenuItem");
        logs.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN));
        logs.setOnAction(e -> eventStudio().broadcast(new SetOverlayItem("LOGS")));

        var helpMenu = new Menu(i18n().tr("Help"));
        helpMenu.getItems().addAll(logs, new SeparatorMenuItem(), about);
        getMenus().addAll(fileMenu, editMenu, helpMenu);
    }

    MenuItem recentProjectMenuItem(String path) {
        var item = new MenuItem(StringUtils.abbreviate(path, path.length(), 60));
        item.setOnAction(a -> eventStudio().broadcast(new LoadProjectRequest(new Project(Paths.get(path)))));
        item.setMnemonicParsing(false);
        return item;
    }
}
