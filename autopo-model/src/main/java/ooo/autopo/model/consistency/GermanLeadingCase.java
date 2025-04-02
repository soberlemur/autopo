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

import java.util.Set;

import static java.lang.Character.isLowerCase;
import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * In German nouns are uppercase so we issue a warning only if the first letter is lowercase
 *
 * @author Andrea Vacondio
 */
class GermanLeadingCase implements ConsistencyValidator {

    private static final Set<String> INCLUDE = Set.of("de");

    @Override
    public String validate(String original, String translated, String targetLanguage) {
        if (isLowerCase(translated.charAt(0))) {
            return i18n().tr("Suspicious lowercase of the first letter");
        }
        return VALID;
    }

    @Override
    public Set<String> includeLanguages() {
        return INCLUDE;
    }
}
