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

import java.util.regex.Pattern;

import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * @author Andrea Vacondio
 */
public class PlaceholdersCount implements ConsistencyValidator {
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
