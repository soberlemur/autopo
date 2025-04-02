package ooo.autopo.app.context;
/*
 * This file is part of the Autopo project
 * Created 13/02/25
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