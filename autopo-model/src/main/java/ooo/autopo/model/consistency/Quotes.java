package ooo.autopo.model.consistency;

/*
 * This file is part of the Autopo project
 * Created 03/02/25
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

import java.util.Map;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * @author Andrea Vacondio
 */
public class Quotes implements ConsistencyValidator {
    private static final Map<String, Character> OPENING_QUOTES_EQUIV = ofEntries(entry("es", '«'),
                                                                                 entry("fr", '«'),
                                                                                 entry("de", '„'),
                                                                                 entry("ru", '«'),
                                                                                 entry("zh", '「'),
                                                                                 entry("ja", '「'),
                                                                                 entry("ko", '《'),
                                                                                 entry("it", '«'),
                                                                                 entry("pt", '«'),
                                                                                 entry("sv", '”'),
                                                                                 entry("no", '«'),
                                                                                 entry("da", '»'),
                                                                                 entry("th", '“'),
                                                                                 entry("lo", '“'),
                                                                                 entry("my", '('),
                                                                                 entry("bo", '༺'),
                                                                                 entry("dz", '༺'));
    private static final Map<String, Character> CLOSING_QUOTES_EQUIV = ofEntries(entry("es", '»'),
                                                                                 entry("fr", '»'),
                                                                                 entry("de", '“'),
                                                                                 entry("ru", '»'),
                                                                                 entry("zh", '」'),
                                                                                 entry("ja", '」'),
                                                                                 entry("ko", '》'),
                                                                                 entry("it", '»'),
                                                                                 entry("pt", '»'),
                                                                                 entry("sv", '”'),
                                                                                 entry("no", '»'),
                                                                                 entry("da", '«'),
                                                                                 entry("th", '”'),
                                                                                 entry("lo", '”'),
                                                                                 entry("my", ')'),
                                                                                 entry("bo", '༻'),
                                                                                 entry("dz", '༻'));

    @Override
    public String validate(String original, String translated, String targetLanguage) {
        var startChar = original.charAt(0);
        var endChar = original.charAt(original.length() - 1);
        if (('"' == startChar || '\'' == startChar) && ('"' == endChar || '\'' == endChar)) {
            if (startChar != translated.charAt(0) || endChar != translated.charAt(translated.length() - 1)) {
                if (translated.charAt(0) != OPENING_QUOTES_EQUIV.getOrDefault(targetLanguage,
                                                                              '"') || translated.charAt(original.length() - 1) != CLOSING_QUOTES_EQUIV.getOrDefault(
                        targetLanguage,
                        '"')) {
                    return i18n().tr("Suspicious quotes inconsistency between original and translation");
                }
            }
        }
        return VALID;
    }
}
