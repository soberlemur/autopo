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
public class ExclamationMark implements ConsistencyValidator {
    private static final Set<String> EXCLUDE = Set.of("hy", "bo", "es", "dz", "gl");

    @Override
    public String validate(String original, String translated, String targetLanguage) {
        if ('!' == original.charAt(original.length() - 1) ^ '!' == translated.charAt(translated.length() - 1)) {
            return i18n().tr("Inconsistent exclamation mark between original and translation");
        }
        return VALID;
    }

    @Override
    public Set<String> excludeLanguages() {
        return EXCLUDE;
    }
}
