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

import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * In Spanish questions are between ¡ and !
 *
 * @author Andrea Vacondio
 */
class SpanishExclamationMark implements ConsistencyValidator {

    private static final Set<String> INCLUDES = Set.of("es", "gl");

    @Override
    public String validate(String original, String translated, String targetLanguage) {
        var last = original.charAt(original.length() - 1);
        var lastTranslated = translated.charAt(translated.length() - 1);
        var firstTranslated = translated.charAt(0);

        if (last == '!' && (lastTranslated != '!' || firstTranslated != '¡')) {
            return i18n().tr("Translated string should be included in ¡ and !");
        }
        if (last != '!' && (lastTranslated == '!' || firstTranslated == '¡')) {
            return i18n().tr("Translated string should not be included in ¡ and !");
        }
        return VALID;
    }

    @Override
    public Set<String> includeLanguages() {
        return INCLUDES;
    }
}
