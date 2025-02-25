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
 * You are not permitted to distribute it in any form unless explicit
 * consent is given by Sober Lemur S.r.l..
 * You are not permitted to modify it.
 *
 * Autopo is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
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