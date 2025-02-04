package ooo.autopo.model.consistency;

/*
 * This file is part of the Autopo project
 * Created 03/02/25
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
