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

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * @author Andrea Vacondio
 */
public class MainMenuBar extends MenuBar {

    public MainMenuBar() {
        var fileMenu = new Menu(i18n().tr("File"));
        var editMenu = new Menu(i18n().tr("Edit"));
        var settingsMenu = new Menu(i18n().tr("Settings"));
        var helpMenu = new Menu(i18n().tr("Help"));
        getMenus().addAll(fileMenu, editMenu, settingsMenu, helpMenu);
    }
}
