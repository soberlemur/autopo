package ooo.autopo.app.ui.components.preferences;

/*
 * This file is part of the Autopo project
 * Created 18/02/25
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
        this.getStyleClass().add("cmb-box");
    }
}
