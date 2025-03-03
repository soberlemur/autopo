package ooo.autopo.model.consistency;

/*
 * This file is part of the Autopo project
 * Created 26/02/25
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

import static java.lang.Character.isUpperCase;
import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * In spanish questions are inlcuded in ¿ and ?, exclamations in ¡ and ! so we have to verify the case of the secondo char, not the first
 *
 * @author Andrea Vacondio
 */
public class SpanishLeadingCase implements ConsistencyValidator {

    private static final Set<String> INCLUDES = Set.of("es", "gl");

    @Override
    public String validate(String original, String translated, String targetLanguage) {
        if (isUpperCase(original.charAt(0)) ^ isUpperCase(getFirstTranslated(translated, original))) {
            return i18n().tr("Inconsistent capitalization of the first letter between the original and translation");
        }
        return VALID;
    }

    private char getFirstTranslated(String translated, String original) {
        var lastOriginal = original.charAt(original.length() - 1);
        if (translated.length() > 1 && (lastOriginal == '?' || lastOriginal == '!')) {
            return translated.charAt(1);
        }
        return translated.charAt(0);
    }

    @Override
    public Set<String> includeLanguages() {
        return INCLUDES;
    }
}
