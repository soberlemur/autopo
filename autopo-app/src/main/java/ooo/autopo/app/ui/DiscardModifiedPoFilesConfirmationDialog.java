package ooo.autopo.app.ui;

/*
 * This file is part of the Autopo project
 * Created 13/03/25
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

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * @author Andrea Vacondio
 */
public class DiscardModifiedPoFilesConfirmationDialog extends Alert {

    public DiscardModifiedPoFilesConfirmationDialog() {
        super(AlertType.CONFIRMATION);
        setTitle(i18n().tr("Discard modification"));
        setHeaderText(i18n().tr("There are modified Po files, loading a new project will discard the modification, do you want to proceed?"));
        var yesBtn = new ButtonType(i18n().tr("Yes"), ButtonBar.ButtonData.YES);
        var noBtn = new ButtonType(i18n().tr("No"), ButtonBar.ButtonData.NO);
        getButtonTypes().setAll(yesBtn, noBtn);
    }
}
