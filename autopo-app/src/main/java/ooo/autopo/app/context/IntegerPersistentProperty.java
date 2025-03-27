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

/**
 * Configurable Integer value property
 *
 * @author Andrea Vacondio
 */
public enum IntegerPersistentProperty implements PersistentProperty<Integer> {
    LOGVIEW_ROWS_NUMBER(() -> 200),
    BATCH_SIZE(() -> 50);

    private final Supplier<Integer> defaultSupplier;

    IntegerPersistentProperty(Supplier<Integer> supplier) {
        this.defaultSupplier = supplier;
    }

    @Override
    public String key() {
        return this.name().toLowerCase();
    }

    @Override
    public Supplier<Integer> defaultSupplier() {
        return defaultSupplier;
    }
}
