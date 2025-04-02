package ooo.autopo.app.context;

import ooo.autopo.app.ConfigurableSystemProperty;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.ClearSystemProperty;
import org.junitpioneer.jupiter.SetSystemProperty;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
class BooleanPersistentPropertyTest {

    @Test
    @SetSystemProperty.SetSystemProperties({ @SetSystemProperty(key = ConfigurableSystemProperty.CHECK_FOR_UPDATES_PROP, value = "false") })
    @DisplayName("Default value suppliers from sys props")
    public void defaultValuesFromSysProp() {
        assertFalse(BooleanPersistentProperty.CHECK_UPDATES.defaultSupplier().get());
    }

    @Test
    @ClearSystemProperty.ClearSystemProperties({ @ClearSystemProperty(key = ConfigurableSystemProperty.CHECK_FOR_UPDATES_PROP) })

    @DisplayName("Default value supplier when no sys props")
    public void defaultValuesClearedSysProp() {
        assertTrue(BooleanPersistentProperty.CHECK_UPDATES.defaultSupplier().get());
    }
}