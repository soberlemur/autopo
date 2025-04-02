package ooo.autopo.app.ui.components.preferences;

/*
 * This file is part of the Autopo project
 * Created 18/02/25
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

import javafx.scene.control.ComboBox;
import ooo.autopo.app.context.ApplicationContext;
import ooo.autopo.app.context.StringPersistentProperty;
import ooo.autopo.model.ui.ComboItem;
import org.tinylog.Logger;

import static ooo.autopo.app.context.ApplicationContext.app;
import static org.sejda.commons.util.RequireUtils.requireNotNullArg;

/**
 * @author Andrea Vacondio
 */
public class PreferenceComboBox<T extends ComboItem<?>> extends ComboBox<T> {

    public PreferenceComboBox(StringPersistentProperty property) {
        this(property, app());
    }

    PreferenceComboBox(StringPersistentProperty property, ApplicationContext context) {
        requireNotNullArg(property, "Preference cannot be null");
        valueProperty().addListener((observable, oldValue, newValue) -> {
            context.persistentSettings().set(property, newValue.key().toString());
            Logger.trace("Preference {} set to {}", property, newValue.key());
        });
    }
}
