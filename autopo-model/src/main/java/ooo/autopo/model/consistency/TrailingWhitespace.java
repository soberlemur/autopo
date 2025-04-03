package ooo.autopo.model.consistency;

/*
 * This file is part of the Autopo project
 * Created 31/01/25
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

import java.util.Locale;
import java.util.Set;

import static java.lang.Character.isWhitespace;
import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * Consistency validator that checks for consistency in the presence of whitespaces at the end of the strings.
 *
 * @author Andrea Vacondio
 */
class TrailingWhitespace implements ConsistencyValidator {

    private static final Set<String> SKIP_FOR = Set.of(Locale.JAPANESE.getLanguage(), Locale.CHINESE.getLanguage(), "th", "lo", "my", "bo", "km", "dz");

    @Override
    public String validate(String original, String translated, String targetLanguage) {
        if (isWhitespace(original.charAt(original.length() - 1)) ^ isWhitespace(translated.charAt(translated.length() - 1))) {
            return i18n().tr("Inconsistent trailing spaces between original and translation");
        }
        return VALID;
    }

    @Override
    public Set<String> excludeLanguages() {
        return SKIP_FOR;
    }
}
