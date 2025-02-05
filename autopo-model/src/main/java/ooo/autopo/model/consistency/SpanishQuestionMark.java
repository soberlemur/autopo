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

import java.util.Set;

import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * @author Andrea Vacondio
 */
public class SpanishQuestionMark implements ConsistencyValidator {

    private static final Set<String> INCLUDES = Set.of("es", "gl");

    @Override
    public String validate(String original, String translated, String targetLanguage) {
        var last = original.charAt(original.length() - 1);
        var lastTranslated = translated.charAt(translated.length() - 1);
        var firstTranslated = translated.charAt(0);

        if (last == '?' && lastTranslated != '?' && firstTranslated != '¿') {
            return i18n().tr("Translated string should be included in ¿ and ?");
        }
        if (last != '?' && (lastTranslated == '?' || firstTranslated == '¿')) {
            return i18n().tr("Translated string should not be included in ¿ and ?");
        }
        return VALID;
    }

    @Override
    public Set<String> includeLanguages() {
        return INCLUDES;
    }
}
