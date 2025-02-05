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

import ooo.autopo.model.PoEntry;

import java.util.Locale;
import java.util.Set;
import java.util.function.BiConsumer;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Andrea Vacondio
 */
public interface ConsistencyValidator extends BiConsumer<PoEntry, Locale> {

    BiConsumer<PoEntry, Locale> VALIDATORS = new FullStop().andThen(new GermanLeadingCase())
                                                           .andThen(new TrailingWhitespace())
                                                           .andThen(new PlaceholdersCount())
                                                           .andThen(new LeadingWhitespace())
                                                           .andThen(new KoreanFullStop())
                                                           .andThen(new LeadingCase())
                                                           .andThen(new Quotes())
                                                           .andThen(new TrailingLinefeed())
                                                           .andThen(new QuestionMark())
                                                           .andThen(new SpanishQuestionMark())
                                                           .andThen(new ExclamationMark());
    String VALID = null;

    /**
     * @return a set of languages for which the validator should not be applied
     */
    default Set<String> excludeLanguages() {
        return Set.of();
    }

    /**
     * If not empty, the validator will be applied to these languages only.
     *
     * @return a set of languages for which the validator should be applied
     */
    default Set<String> includeLanguages() {
        return Set.of();
    }

    default void accept(PoEntry entry, Locale locale) {
        //we validate only if there is a translated value and if we shouldn't skip for the given target locale
        var language = locale.getLanguage();
        if (isNotBlank(entry.value().getValue()) && shouldValidate(language)) {
            ofNullable(validate(entry.key().getValue(), entry.value().getValue(), language)).ifPresent(entry::addWarning);
        }
    }

    private boolean shouldValidate(String language) {
        if (includeLanguages().isEmpty()) {
            return !excludeLanguages().contains(language);
        }
        return includeLanguages().contains(language);
    }

    /**
     * Applies the consistency validation to the given original String and translated String
     *
     * @return the warning if triggered by this validation, null if the validation succeded
     */
    String validate(String original, String translated, String targetLanguage);
}
