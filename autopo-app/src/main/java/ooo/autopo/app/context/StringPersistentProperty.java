package ooo.autopo.app.context;
/*
 * This file is part of the Autopo project
 * Created 13/02/25
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

import java.util.function.Supplier;

import static ooo.autopo.app.ConfigurableSystemProperty.LOCALE_PROP;

/**
 * Configurable String value property
 *
 * @author Andrea Vacondio
 */
public enum StringPersistentProperty implements PersistentProperty<String> {
    STARTUP_PROJECT_PATH(() -> ""),
    LOCALE(() -> System.getProperty(LOCALE_PROP)),
    FONT_SIZE(() -> "");

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
