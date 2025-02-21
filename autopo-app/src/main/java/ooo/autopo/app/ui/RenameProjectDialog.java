package ooo.autopo.app.ui;

/*
 * This file is part of the Autopo project
 * Created 21/02/25
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

import javafx.beans.binding.Bindings;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import org.apache.commons.lang3.StringUtils;

import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * @author Andrea Vacondio
 */
public class RenameProjectDialog extends TextInputDialog {

    public RenameProjectDialog(String initialValue) {
        super(initialValue);
        setTitle(i18n().tr("Rename project"));
        setContentText(i18n().tr("Enter the new name for the project:"));
        setHeaderText(null);
        var textProperty = this.getEditor().textProperty();
        this.getDialogPane()
            .lookupButton(ButtonType.OK)
            .disableProperty()
            .bind(Bindings.createBooleanBinding(() -> StringUtils.isBlank(textProperty.get()), textProperty));
    }
}
