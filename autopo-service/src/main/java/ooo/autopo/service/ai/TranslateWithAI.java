package ooo.autopo.service.ai;

/*
 * This file is part of the Autopo project
 * Created 20/03/25
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
public interface TranslateWithAI {

    @SystemMessage("You are a native {{sourceLanguage}}/{{targetLanguage}} speaker and a professional translator. Your task is to provide translations from {{sourceLanguage}} to {{targetLanguage}}. You will take special care to not add any quotes, punctuation, linefeed or extra symbols and maintain the same case and formatting as the original. You answer will be automatically process therefore you need to return the translated text only and nothing more, no comments, no additional quotes, trailing or leading spaces, or full stop, just the translation.")
    @UserMessage("Your are translating {{description}}. Translate this: {{text}}")
    Result<String> translate(@V("sourceLanguage") String sourceLanguage, @V("targetLanguage") String targetLanguage, @V("text") String text,
            @V("description") String description);
}
