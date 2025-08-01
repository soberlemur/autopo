package ooo.autopo.model.consistency;

/*
 * This file is part of the Autopo project
 * Created 02/02/25
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

import static java.lang.Character.isUpperCase;
import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * Consistency validator that checks for consistency in the capitalization of the input and translated string.
 *
 * @author Andrea Vacondio
 */
class LeadingCase implements ConsistencyValidator {

    private static final Set<String> EXCLUDE = Set.of(Locale.JAPANESE.getLanguage(),
                                                      Locale.CHINESE.getLanguage(),
                                                      "ar",
                                                      "th",
                                                      "lo",
                                                      "my",
                                                      "bo",
                                                      "km",
                                                      "dz",
                                                      "ko",
                                                      "ka",
                                                      "de",
                                                      "es",
                                                      "gl",
                                                      "he",
                                                      "ckb",
                                                      "kmr",
                                                      "hi");

    @Override
    public String validate(String original, String translated, String targetLanguage) {
        if (isUpperCase(original.charAt(0)) ^ isUpperCase(translated.charAt(0))) {
            return i18n().tr("Inconsistent capitalization of the first letter between the original and translation");
        }
        return VALID;
    }

    @Override
    public Set<String> excludeLanguages() {
        return EXCLUDE;
    }
}
