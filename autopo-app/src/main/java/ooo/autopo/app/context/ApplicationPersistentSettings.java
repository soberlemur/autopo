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

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import org.pdfsam.persistence.PersistenceException;
import org.pdfsam.persistence.PreferencesRepository;
import org.tinylog.Logger;

import java.util.Arrays;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static ooo.autopo.i18n.I18nContext.i18n;
import static org.sejda.commons.util.RequireUtils.requireNotNullArg;

/**
 * @author Andrea Vacondio
 */
public class ApplicationPersistentSettings {

    private final PreferencesRepository repo;
    private final SimpleObjectProperty<PersistentPropertyChange<String>> stringSettingsChanges = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<PersistentPropertyChange<Integer>> intSettingsChanges = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<PersistentPropertyChange<Boolean>> boolSettingsChanges = new SimpleObjectProperty<>();

    ApplicationPersistentSettings(PreferencesRepository repo) {
        this.repo = repo;
    }

    /**
     * @param prop
     * @return the value for the given {@link StringPersistentProperty} or the default associated value
     */
    public Optional<String> get(StringPersistentProperty prop) {
        requireNotNullArg(prop, "Cannot get value for a null property");
        try {
            return ofNullable(this.repo.getString(prop.key(), prop.defaultSupplier()));
        } catch (PersistenceException e) {
            Logger.error(e, "Unable to get persistent property: {}", prop);
        }
        return ofNullable(prop.defaultSupplier().get());
    }

    /**
     * @param prop
     * @return the value of the given {@link IntegerPersistentProperty} or the default associated value
     */
    public int get(IntegerPersistentProperty prop) {
        requireNotNullArg(prop, "Cannot get value for a null property");
        try {
            return this.repo.getInt(prop.key(), prop.defaultSupplier());
        } catch (PersistenceException e) {
            Logger.error(e, "Unable to get persistent property: {}", prop);
        }
        return prop.defaultSupplier().get();
    }

    /**
     * @param prop
     * @return the value of the given {@link BooleanPersistentProperty} or the default associated value
     */
    public boolean get(BooleanPersistentProperty prop) {
        requireNotNullArg(prop, "Cannot get value for a null property");
        try {
            return this.repo.getBoolean(prop.key(), prop.defaultSupplier());
        } catch (NumberFormatException | PersistenceException e) {
            Logger.error(e, "Unable to get persistent property: {}", prop);
        }
        return prop.defaultSupplier().get();
    }

    /**
     * Persists the given String property key/value
     */
    public void set(StringPersistentProperty prop, String value) {
        requireNotNullArg(prop, "Cannot set value for a null property");
        try {
            this.repo.saveString(prop.key(), value);
            stringSettingsChanges.set(new PersistentPropertyChange<>(prop, ofNullable(value)));
        } catch (PersistenceException e) {
            Logger.error(e, "Unable to save persistent property: {}", prop);
        }

    }

    /**
     * Persists the given Integer property key/value
     */
    public void set(IntegerPersistentProperty prop, int value) {
        requireNotNullArg(prop, "Cannot set value for a null property");
        try {
            this.repo.saveInt(prop.key(), value);
            intSettingsChanges.set(new PersistentPropertyChange<>(prop, of(value)));
        } catch (PersistenceException e) {
            Logger.error(e, "Unable to save persistent property: {}", prop);
        }
    }

    /**
     * Persists the given Boolean property key/value
     */
    public void set(BooleanPersistentProperty prop, boolean value) {
        requireNotNullArg(prop, "Cannot set value for a null property");
        try {
            this.repo.saveBoolean(prop.key(), value);
            boolSettingsChanges.set(new PersistentPropertyChange<>(prop, of(value)));
        } catch (PersistenceException e) {
            Logger.error(e, "Unable to save persistent property: {}", prop);
        }
    }

    /**
     * @return true if there is a value stored for the given persistent property
     */
    public boolean hasValueFor(PersistentProperty<?> property) {
        if (nonNull(property)) {
            return Arrays.stream(this.repo.keys()).anyMatch(k -> k.equals(property.key()));
        }
        return false;
    }

    /**
     * Deletes the value stored for the given property
     */
    public void delete(PersistentProperty<?> property) {
        if (nonNull(property)) {
            this.repo.delete(property.key());
        }
    }

    /**
     * @return an observable for changes to the given property
     */
    public ObservableValue<Optional<String>> settingsChanges(StringPersistentProperty prop) {
        var value = new SimpleObjectProperty<Optional<String>>(empty());
        stringSettingsChanges.subscribe((old, c) -> {
            if (c.property().equals(prop)) {
                value.set(c.value());
            }
        });
        return value;
    }

    /**
     * @return an observable for changes to the given property
     */
    public ObservableValue<Optional<Integer>> settingsChanges(IntegerPersistentProperty prop) {
        var value = new SimpleObjectProperty<Optional<Integer>>(empty());
        intSettingsChanges.subscribe((old, c) -> {
            if (c.property().equals(prop)) {
                value.set(c.value());
            }
        });
        return value;
    }

    /**
     * @return an observable for changes to the given property
     */
    public ObservableValue<Optional<Boolean>> settingsChanges(BooleanPersistentProperty prop) {
        var value = new SimpleObjectProperty<Optional<Boolean>>(empty());
        boolSettingsChanges.subscribe((old, c) -> {
            if (c.property().equals(prop)) {
                value.set(c.value());
            }
        });
        return value;
    }

    /**
     * Clears all the persistent settings
     */
    public void clean() {
        try {
            this.repo.clean();
            Logger.info(i18n().tr("Persistent application settings deleted"));
        } catch (PersistenceException e) {
            Logger.error(e, i18n().tr("Unable to clear application settings"));
        }
    }
}
