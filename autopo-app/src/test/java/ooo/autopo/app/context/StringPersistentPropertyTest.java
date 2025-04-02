package ooo.autopo.app.context;

import ooo.autopo.app.ConfigurableSystemProperty;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.ClearSystemProperty;
import org.junitpioneer.jupiter.SetSystemProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/*
 * This file is part of the Autopo project
 * Created 25/02/25
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
class StringPersistentPropertyTest {
    @Test
    @SetSystemProperty.SetSystemProperties({ @SetSystemProperty(key = ConfigurableSystemProperty.LOCALE_PROP, value = "es"),
            @SetSystemProperty(key = ConfigurableSystemProperty.THEME_PROP, value = "DARK"), })
    @DisplayName("Default value supplier from sys props")
    public void defaultValuesFromSysProp() {
        assertEquals("es", StringPersistentProperty.LOCALE.defaultSupplier().get());
        assertEquals("DARK", StringPersistentProperty.THEME.defaultSupplier().get());
    }

    @Test
    @ClearSystemProperty.ClearSystemProperties({ @ClearSystemProperty(key = ConfigurableSystemProperty.LOCALE_PROP),
            @ClearSystemProperty(key = ConfigurableSystemProperty.THEME_PROP) })
    @DisplayName("Default value supplier when no sys props")
    public void defaultValuesClearedSysProp() {
        assertNull(StringPersistentProperty.LOCALE.defaultSupplier().get());
        assertNull(StringPersistentProperty.THEME.defaultSupplier().get());
    }

    @Test
    public void defaultValue() {
        assertEquals("", StringPersistentProperty.FONT_SIZE.defaultSupplier().get());
        assertEquals("", StringPersistentProperty.STARTUP_PROJECT_PATH.defaultSupplier().get());
    }
}