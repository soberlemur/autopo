package ooo.autopo.app.ui;

/*
 * This file is part of the Autopo project
 * Created 21/02/25
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
