package ooo.autopo.i18n;
/*
 * This file is part of the Autopo project
 * Created 30/01/25
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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Andrea Vacondio
 */
public class SetLocaleEventTest {
    @Test
    public void nullArg() {
        assertThrows(IllegalArgumentException.class, () -> new SetLocaleRequest(null));
    }

    @Test
    public void blankArg() {
        assertThrows(IllegalArgumentException.class, () -> new SetLocaleRequest("  "));
    }

    @Test
    public void notNullArg() {
        var victim = new SetLocaleRequest("ChuckNorris");
        assertEquals("ChuckNorris", victim.languageTag());
    }
}
