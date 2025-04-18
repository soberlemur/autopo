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

import java.util.regex.Pattern;

import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * Validator that checks for consistency in the number of placeholders between the original and translated strings. Placeholders include formats like %s, %d,
 * %c, %x, %b, %f, and {0}.
 *
 * @author Andrea Vacondio
 */
class PlaceholdersCount implements ConsistencyValidator {
    private static final Pattern PATTERN = Pattern.compile("%[sdcxbf]|\\{[0-9]}");

    @Override
    public String validate(String original, String translated, String targetLanguage) {
        if (original.indexOf('{') != 0 || original.indexOf('%') != 0) {
            if (PATTERN.matcher(original).results().count() != PATTERN.matcher(translated).results().count()) {
                return i18n().tr("Inconsistent number of placeholders (%s, %d, %c, %x, %b, %f, {0}...) between original and translation");
            }
        }
        return VALID;
    }

}
