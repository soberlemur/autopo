package ooo.autopo.service.ai;

/*
 * This file is part of the Autopo project
 * Created 20/03/25
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

import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import ooo.autopo.model.ai.TranslationAssessment;

/**
 * @author Andrea Vacondio
 */
public interface TranslationServiceAI {

    @SystemMessage("You are a native {{sourceLanguage}}/{{targetLanguage}} speaker and a professional translator. Your task is to provide translations from {{sourceLanguage}} to {{targetLanguage}}. You will take special care to not add any quotes, punctuation, linefeed or extra symbols and maintain the same case and formatting as the original. Your answer will be automatically processed therefore you need to return the translated text only and nothing more, no comments, no additional quotes, trailing or leading spaces, or full stop just the translation.")
    @UserMessage("Your are translating {{description}}. Translate this: \"{{untranslated}}\"")
    Result<String> translate(@V("sourceLanguage") String sourceLanguage, @V("targetLanguage") String targetLanguage,
            @V("description") String description, @V("untranslated") String untranslated);

    @SystemMessage("You are a professional linguist and translation quality evaluator. Your task is to assess the accuracy, fluency, and naturalness of a translation from {{sourceLanguage}} to {{targetLanguage}}. This is the context of the translation: \"{{description}}\".\nConsider factors such as correctness of meaning, grammar, style, idiomatic expressions, cultural appropriateness, punctuation, and case sensitivity. Pay special attention to terminology and tone relevant to the specified context\n" + "\n" + "Provide a score from 1 to 10, with 10 being a perfect translation. If and only if the score is less than 10, also provide:\n" + "\n" + " 1: Feedback on the translation quality and recommendations for improvement.\n" + "\n" + " 2: A suggested replacement translation that better fits the context.")
    @UserMessage("This is the original text: \"{{untranslated}}\"\n" + "\n" + "This is the translation to evaluate: \"{{translated}}\"")
    Result<TranslationAssessment> assess(@V("sourceLanguage") String sourceLanguage, @V("targetLanguage") String targetLanguage,
            @V("description") String description, @V("untranslated") String untranslated, @V("translated") String translated);

}
