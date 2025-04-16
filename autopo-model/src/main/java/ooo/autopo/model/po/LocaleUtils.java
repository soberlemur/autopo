package ooo.autopo.model.po;

/*
 * This file is part of the Autopo project
 * Created 16/04/25
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

import org.tinylog.Logger;

import java.util.IllformedLocaleException;
import java.util.Locale;

import static ooo.autopo.i18n.I18nContext.i18n;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Utility class to handle locales and language tag in .po files
 *
 * @author Andrea Vacondio
 */
public final class LocaleUtils {
    private LocaleUtils() {
        //hide
    }

    /**
     * @return The Locale corresponding to the given language header as defined <a
     * href="https://www.gnu.org/software/gettext/manual/html_node/Header-Entry.html">here</a>. Returns null if the input header is blank.
     */
    public static Locale localeFromHeader(String languageHeader) {
        if (isNotBlank(languageHeader)) {
            Logger.debug(i18n().tr("Trying to guess locale from '{}'"), languageHeader);
            var headerFragments = languageHeader.split("[@_]");
            if (headerFragments.length > 0) {
                try {
                    var builder = new Locale.Builder().setLanguage(headerFragments[0]);
                    if (headerFragments.length > 1) {
                        builder.setRegion(headerFragments[1]);
                        if (headerFragments.length > 2) {
                            builder.setVariant(headerFragments[2]);
                        }
                    }

                    return builder.build();
                } catch (IllformedLocaleException e) {
                    Logger.warn(i18n().tr("Invalid locale: {}"), languageHeader);
                }
            }
        }
        return null;
    }

    /**
     * @return a Locale guessed from the given filename or null if unable to identify the locale
     */
    public static Locale localeFromFilename(String name) {
        if (isNotBlank(name)) {
            Logger.debug(i18n().tr("Trying to guess locale from '{}'"), name);
            try {
                var locale = org.apache.commons.lang3.LocaleUtils.toLocale(name);
                if (isNotBlank(locale.getLanguage())) {
                    return locale;
                }
            } catch (IllegalArgumentException e) {
                Logger.warn(i18n().tr("Invalid locale: {}"), name);
            }
        }
        return null;
    }

    /**
     * @return the language header corresponding to the given locale as defined <a
     * href="https://www.gnu.org/software/gettext/manual/html_node/Header-Entry.html">here</a>
     */
    public static String languageHeaderFromLocale(Locale locale) {
        String languageTag = locale.getLanguage();

        if (isNotBlank(locale.getCountry())) {
            languageTag += "_" + locale.getCountry().toUpperCase();
        }

        if (isNotBlank(locale.getVariant())) {
            languageTag += "@" + locale.getVariant().toLowerCase();
        }
        return languageTag;
    }
}
