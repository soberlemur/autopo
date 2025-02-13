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

import static ooo.autopo.app.ConfigurableSystemProperty.CHECK_FOR_UPDATES_PROP;

/**
 * @author Andrea Vacondio
 */
public enum BooleanPersistentProperty implements PersistentProperty<Boolean> {

    CHECK_UPDATES(() -> Boolean.parseBoolean(System.getProperty(CHECK_FOR_UPDATES_PROP, Boolean.TRUE.toString())));

    private final Supplier<Boolean> defaultSupplier;

    BooleanPersistentProperty(Supplier<Boolean> supplier) {
        this.defaultSupplier = supplier;
    }

    @Override
    public String key() {
        return this.name().toLowerCase();
    }

    @Override
    public Supplier<Boolean> defaultSupplier() {
        return defaultSupplier;
    }
}