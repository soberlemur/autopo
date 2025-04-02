package ooo.autopo.i18n;
/*
 * This file is part of the Autopo project
 * Created 30/01/25
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
import org.pdfsam.eventstudio.annotation.EventListener;
import org.tinylog.Logger;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import static java.util.Objects.nonNull;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * Context to deal with translations. It contains information about the mutable current locale and allow to translate strings to the current locale through
 * static methods.
 *
 * @author Andrea Vacondio
 */
public final class I18nContext {

    private final Set<Locale> supported = Set.of(Locale.ITALIAN, Locale.of("es"), Locale.UK);

    private final SimpleObjectProperty<Locale> locale = new SimpleObjectProperty<>();
    private Optional<ResourceBundle> bundle = empty();

    I18nContext() {
        eventStudio().addAnnotatedListeners(this);
        locale.subscribe(this::loadBundles);
    }

    @EventListener
    public void setLocale(SetLocaleRequest e) {
        if (nonNull(e.languageTag()) && !e.languageTag().isBlank()) {
            Logger.trace("Setting locale to {}", e.languageTag());
            ofNullable(Locale.forLanguageTag(e.languageTag())).filter(supported::contains).ifPresent(locale::set);
        }
    }

    private void loadBundles(Locale l) {
        if (nonNull(l)) {
            Locale.setDefault(l);
            Logger.trace("Loading i18n bundle for {}", Locale.getDefault());
            try {
                this.bundle = ofNullable(ResourceBundle.getBundle("ooo.autopo.i18n.Messages", Locale.getDefault(),
                                                                  I18nContext.class.getModule()));
                Logger.debug("Locale set to {}", Locale.getDefault());
            } catch (Exception e) {
                Logger.error("Unable to load translations bundle", e);
            }
        }
    }

    Locale getBestLocale() {
        if (supported.contains(Locale.getDefault())) {
            Logger.trace("Using best matching locale: {}", Locale.getDefault());
            return Locale.getDefault();
        }
        var onlyLanguage = Locale.of(Locale.getDefault().getLanguage());
        if (supported.contains(onlyLanguage)) {
            Logger.trace("Using supported locale closest to default {}", onlyLanguage);
            return onlyLanguage;
        }
        Logger.trace("Using fallback locale");
        return Locale.ENGLISH;
    }

    /**
     * @return the default {@link I18nContext} instance
     */
    public static I18nContext i18n() {
        return I18nContextHolder.CONTEXT;
    }

    /**
     * @return an {@link ObservableValue} {@link Locale} representing the current locale
     */
    public ObservableValue<Locale> locale() {
        return this.locale;
    }

    public String tr(String text) {
        initBundleIfRequired();
        return bundle.filter(r -> r.containsKey(text)).map(r -> r.getString(text)).orElse(text);
    }

    /**
     * @param text    text to be translated
     * @param replace replacements for the placeholders
     * @return the translated string where {0} and {1} (etc) placeholders are replaced by the replace[0], replace[1] etc
     */
    public String tr(String text, String... replace) {
        initBundleIfRequired();
        return MessageFormat.format(tr(text), (Object[]) replace);
    }

    private void initBundleIfRequired() {
        if (bundle.isEmpty()) {
            locale.set(getBestLocale());
        }
    }

    public Set<Locale> getSupported() {
        return supported;
    }

    /**
     * Lazy initialization holder class idiom (Joshua Bloch, Effective Java second edition, item 71).
     *
     * @author Andrea Vacondio
     */
    private static final class I18nContextHolder {

        private I18nContextHolder() {
            // hide constructor
        }

        static final I18nContext CONTEXT = new I18nContext();
    }
}
