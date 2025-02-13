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
 * Property that can be persisted and may have a default value.
 *
 * @author Andrea Vacondio
 */
public interface PersistentProperty<T> {
    /**
     * @return the key to store this property
     */
    String key();

    /**
     * @return default value provider, it must never be null
     */
    Supplier<T> defaultSupplier();
}
