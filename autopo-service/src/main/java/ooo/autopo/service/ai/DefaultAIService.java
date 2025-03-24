package ooo.autopo.service.ai;

/*
 * This file is part of the Autopo project
 * Created 14/02/25
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

import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.Result;
import ooo.autopo.model.ai.AIModelDescriptor;
import ooo.autopo.model.po.PoEntry;
import ooo.autopo.model.po.PoFile;
import org.tinylog.Logger;

import java.util.Locale;

/**
 * @author Andrea Vacondio
 */
public class DefaultAIService implements AIService {
    @Override
    public String languageTagFor(String string) {
        return "";
    }

    @Override
    public Result<String> translate(PoFile poFile, PoEntry entry, AIModelDescriptor aiModelDescriptor, String projectDescription) {
        Logger.info("Translating using AI model {}", aiModelDescriptor.name());
        TranslateWithAI aiService = AiServices.create(TranslateWithAI.class, aiModelDescriptor.model());

        return aiService.translate(Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH),
                                   poFile.locale().getDisplayLanguage(Locale.ENGLISH),
                                   entry.untranslatedValue().getValue(),
                                   projectDescription);

    }
}
