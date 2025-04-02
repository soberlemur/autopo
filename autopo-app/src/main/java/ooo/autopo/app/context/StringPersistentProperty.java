package ooo.autopo.app.context;
/*
 * This file is part of the Autopo project
 * Created 13/02/25
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

import java.util.function.Supplier;

import static ooo.autopo.app.ConfigurableSystemProperty.LOCALE_PROP;
import static ooo.autopo.app.ConfigurableSystemProperty.THEME_PROP;

/**
 * Configurable String value property
 *
 * @author Andrea Vacondio
 */
public enum StringPersistentProperty implements PersistentProperty<String> {
    STARTUP_PROJECT_PATH(() -> ""),
    LOCALE(() -> System.getProperty(LOCALE_PROP)),
    THEME(() -> System.getProperty(THEME_PROP)),
    FONT_SIZE(() -> ""),
    AI_MODEL(() -> ""),
    VALIDATION_AI_MODEL(() -> "");

    private final Supplier<String> defaultSupplier;

    StringPersistentProperty(Supplier<String> supplier) {
        this.defaultSupplier = supplier;
    }

    @Override
    public String key() {
        return this.name().toLowerCase();
    }

    @Override
    public Supplier<String> defaultSupplier() {
        return defaultSupplier;
    }
}
