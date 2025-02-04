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

import java.util.Map;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * @author Andrea Vacondio
 */
public class Quotes implements ConsistencyValidator {
    private static final Map<String, Character> OPENING_QUOTES_EQUIV = ofEntries(entry("es", '«'),
                                                                                 entry("fr", '«'),
                                                                                 entry("de", '„'),
                                                                                 entry("ru", '«'),
                                                                                 entry("zh", '「'),
                                                                                 entry("ja", '「'),
                                                                                 entry("ko", '《'),
                                                                                 entry("it", '«'),
                                                                                 entry("pt", '«'),
                                                                                 entry("sv", '”'),
                                                                                 entry("no", '«'),
                                                                                 entry("da", '»'),
                                                                                 entry("th", '“'),
                                                                                 entry("lo", '“'),
                                                                                 entry("my", '('),
                                                                                 entry("bo", '༺'),
                                                                                 entry("dz", '༺'));
    private static final Map<String, Character> CLOSING_QUOTES_EQUIV = ofEntries(entry("es", '»'),
                                                                                 entry("fr", '»'),
                                                                                 entry("de", '“'),
                                                                                 entry("ru", '»'),
                                                                                 entry("zh", '」'),
                                                                                 entry("ja", '」'),
                                                                                 entry("ko", '》'),
                                                                                 entry("it", '»'),
                                                                                 entry("pt", '»'),
                                                                                 entry("sv", '”'),
                                                                                 entry("no", '»'),
                                                                                 entry("da", '«'),
                                                                                 entry("th", '”'),
                                                                                 entry("lo", '”'),
                                                                                 entry("my", ')'),
                                                                                 entry("bo", '༻'),
                                                                                 entry("dz", '༻'));

    @Override
    public String validate(String original, String translated, String targetLanguage) {
        var startChar = original.charAt(0);
        var endChar = original.charAt(original.length() - 1);
        if (('"' == startChar || '\'' == startChar) && ('"' == endChar || '\'' == endChar)) {
            if (startChar != translated.charAt(0) || endChar != translated.charAt(translated.length() - 1)) {
                if (translated.charAt(0) != OPENING_QUOTES_EQUIV.getOrDefault(targetLanguage,
                                                                              '"') || translated.charAt(original.length() - 1) != CLOSING_QUOTES_EQUIV.getOrDefault(
                        targetLanguage,
                        '"')) {
                    return i18n().tr("Suspicious quotes inconsistency between original and translation");
                }
            }
        }
        return VALID;
    }
}
