package ooo.autopo.model.consistency;

/*
 * This file is part of the Autopo project
 * Created 31/01/25
 * Copyright 2025 by Sober Lemur S.r.l. (info@soberlemur.com).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import ooo.autopo.model.po.PoEntry;

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
                                                           .andThen(new ExclamationMark())
                                                           .andThen(new SpanishExclamationMark())
                                                           .andThen(new SpanishLeadingCase());
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
        if (isNotBlank(entry.translatedValue().getValue()) && shouldValidate(language)) {
            ofNullable(validate(entry.key().msgId(), entry.translatedValue().getValue(), language)).ifPresent(entry::addWarning);
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
