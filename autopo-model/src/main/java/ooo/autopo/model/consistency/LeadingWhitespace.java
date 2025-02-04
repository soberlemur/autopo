package ooo.autopo.model.consistency;

/*
 * This file is part of the Autopo project
 * Created 31/01/25
 * Copyright 2025 by Sober Lemur S.r.l. (info@soberlemur.com).
 *
 * You are not permitted to distribute it in any form unless explicit
 * consent is given by Sober Lemur S.r.l..
 * You are not permitted to modify it.
 *
 * Autopo is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

import java.util.Locale;
import java.util.Set;

import static java.lang.Character.isWhitespace;
import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * @author Andrea Vacondio
 */
class LeadingWhitespace implements ConsistencyValidator {

    private static final Set<String> EXCLUDE = Set.of(Locale.JAPANESE.getLanguage(), Locale.CHINESE.getLanguage(), "th", "lo", "my", "bo", "km", "dz");

    @Override
    public String validate(String original, String translated, String targetLanguage) {
        if (isWhitespace(original.charAt(0)) ^ isWhitespace(translated.charAt(0))) {
            return i18n().tr("Inconsistent leading spaces between original and translation");
        }
        return VALID;
    }

    @Override
    public Set<String> excludeLanguages() {
        return EXCLUDE;
    }
}
