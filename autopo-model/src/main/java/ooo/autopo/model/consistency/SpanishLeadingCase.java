package ooo.autopo.model.consistency;

/*
 * This file is part of the Autopo project
 * Created 26/02/25
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

import java.util.Set;

import static java.lang.Character.isUpperCase;
import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * In spanish questions are inlcuded in ¿ and ?, exclamations in ¡ and ! so we have to verify the case of the secondo char, not the first
 *
 * @author Andrea Vacondio
 */
public class SpanishLeadingCase implements ConsistencyValidator {

    private static final Set<String> INCLUDES = Set.of("es", "gl");

    @Override
    public String validate(String original, String translated, String targetLanguage) {
        if (isUpperCase(original.charAt(0)) ^ isUpperCase(getFirstTranslated(translated, original))) {
            return i18n().tr("Inconsistent capitalization of the first letter between the original and translation");
        }
        return VALID;
    }

    private char getFirstTranslated(String translated, String original) {
        var lastOriginal = original.charAt(original.length() - 1);
        if (translated.length() > 1 && (lastOriginal == '?' || lastOriginal == '!')) {
            return translated.charAt(1);
        }
        return translated.charAt(0);
    }

    @Override
    public Set<String> includeLanguages() {
        return INCLUDES;
    }
}
