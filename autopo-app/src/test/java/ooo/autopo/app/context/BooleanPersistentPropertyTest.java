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
 * You are not permitted to distribute it in any form unless explicit
 * consent is given by Sober Lemur S.r.l..
 * You are not permitted to modify it.
 *
 * Autopo is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
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