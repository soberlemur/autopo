package ooo.autopo.service.ai;

/*
 * This file is part of the Autopo project
 * Created 25/03/25
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

import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * @author Andrea Vacondio
 */
public interface LocaleWithAI {
    
    @SystemMessage("You are a language identification expert with deep knowledge of linguistic patterns, scripts, and language codes. Given a set of input strings in an unidentified language, return the most appropriate IETF BCP 47 language tag that best represents the detected language. Consider script variations, regional distinctions, and standardization when selecting the tag. If multiple tags are possible, return the most widely accepted or commonly used variant. Your answer will be automatically processed therefore you need to return the language tag only and nothing more, no comments, no additional quotes, trailing or leading spaces, or full stop just the language tag. Example input: ['Bonjour tout le monde', 'Ceci est un test']. Expected output: fr")
    @UserMessage("These are the input strings: {{text}}")
    Result<String> languageTagFor(@V("text") String text);
}
