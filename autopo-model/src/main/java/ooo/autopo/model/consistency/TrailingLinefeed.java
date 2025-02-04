package ooo.autopo.model.consistency;

/*
 * This file is part of the Autopo project
 * Created 01/02/25
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

import static ooo.autopo.i18n.I18nContext.i18n;
import static org.apache.commons.lang3.CharUtils.LF;

/**
 * @author Andrea VacondioF
 */
class TrailingLinefeed implements ConsistencyValidator {

    private static final Set<String> SKIP_FOR = Set.of(Locale.JAPANESE.getLanguage(), Locale.CHINESE.getLanguage(), "th", "lo", "my", "bo", "km", "dz");

    @Override
    public String validate(String original, String translated, String targetLanguage) {
        if (LF == original.charAt(original.length() - 1) ^ LF == translated.charAt(translated.length() - 1)) {
            return i18n().tr("Inconsistent trailing linefeed between original and translation");
        }
        return VALID;
    }

    @Override
    public Set<String> excludeLanguages() {
        return SKIP_FOR;
    }
}
