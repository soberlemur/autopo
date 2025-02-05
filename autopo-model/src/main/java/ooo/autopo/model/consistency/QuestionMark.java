package ooo.autopo.model.consistency;

/*
 * This file is part of the Autopo project
 * Created 05/02/25
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

import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * @author Andrea Vacondio
 */
public class QuestionMark implements ConsistencyValidator {

    private static final Set<String> EXCLUDE = Set.of("ar", "fa", "ur", "syr", "hy", "bo", "dz", "es", "gl");

    private static final Map<String, Character> QMARK_EQUIV = ofEntries(entry("el", ';'));

    @Override
    public String validate(String original, String translated, String targetLanguage) {
        var last = original.charAt(original.length() - 1);
        var lastTranslated = translated.charAt(translated.length() - 1);

        if (last == '?' && lastTranslated != QMARK_EQUIV.getOrDefault(targetLanguage, '?')) {
            return i18n().tr("Inconsistent question mark between original and translation");
        }
        if (last != '?' && (lastTranslated == QMARK_EQUIV.getOrDefault(targetLanguage, '?'))) {
            return i18n().tr("Inconsistent question mark between original and translation");
        }
        return VALID;
    }

    @Override
    public Set<String> excludeLanguages() {
        return EXCLUDE;
    }
}
