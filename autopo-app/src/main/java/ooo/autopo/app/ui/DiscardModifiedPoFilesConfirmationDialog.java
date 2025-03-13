package ooo.autopo.app.ui;

/*
 * This file is part of the Autopo project
 * Created 13/03/25
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
