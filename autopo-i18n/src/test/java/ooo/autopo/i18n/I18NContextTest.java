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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
public class I18NContextTest {

    private I18nContext victim;

    @BeforeEach
    public void before() {
        this.victim = new I18nContext();
    }

    @AfterEach
    public void after() {
        eventStudio().clear();
    }

    @Test
    public void setLocaleSupported() {
        eventStudio().broadcast(new SetLocaleRequest("it"));
        assertEquals(Locale.ITALIAN, victim.locale().getValue());

    }

    @Test
    public void getBestLocaleSupported() {
        Locale.setDefault(Locale.ITALIAN);
        assertNull(victim.locale().getValue());
        victim.tr("chuck norris");
        assertEquals(Locale.ITALIAN, victim.locale().getValue());
    }

    @Test
    public void getBestLocaleSupportedLanguage() {
        Locale.setDefault(Locale.of("en", "CA"));
        assertNull(victim.locale().getValue());
        victim.tr("chuck norris");
        assertEquals(Locale.ENGLISH, victim.locale().getValue());
    }

    @Test
    public void getBestLocaleNotSupportedLanguage() {
        Locale.setDefault(Locale.of("mn", "MN"));
        assertNull(victim.locale().getValue());
        victim.tr("chuck norris");
        assertEquals(Locale.ENGLISH, victim.locale().getValue());
    }
}
