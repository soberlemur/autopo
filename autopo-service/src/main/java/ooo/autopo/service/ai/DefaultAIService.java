package ooo.autopo.service.ai;

/*
 * This file is part of the Autopo project
 * Created 14/02/25
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

import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.Result;
import ooo.autopo.model.ai.AIModelDescriptor;
import ooo.autopo.model.ai.TranslationAssessment;
import ooo.autopo.model.po.PoEntry;
import ooo.autopo.model.po.PoFile;
import org.tinylog.Logger;

import java.util.Locale;

/**
 * @author Andrea Vacondio
 */
public class DefaultAIService implements AIService {
    @Override
    public Result<String> languageTagFor(AIModelDescriptor aiModelDescriptor, String string) {
        Logger.info("Identifying locale using AI model {}", aiModelDescriptor.name());
        LocaleDetectionServiceAI localeIdentifier = AiServices.create(LocaleDetectionServiceAI.class, aiModelDescriptor.translationModel());
        return localeIdentifier.languageTagFor(string);
    }

    @Override
    public Result<String> translate(PoFile poFile, PoEntry entry, AIModelDescriptor aiModelDescriptor, String projectDescription) {
        Logger.info("Translating using AI model {}", aiModelDescriptor.name());
        TranslationServiceAI aiService = AiServices.create(TranslationServiceAI.class, aiModelDescriptor.translationModel());

        return aiService.translate(Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH),
                                   poFile.locale().get().getDisplayLanguage(Locale.ENGLISH),
                                   projectDescription,
                                   entry.untranslatedValue().getValue());

    }

    @Override
    public Result<TranslationAssessment> assess(PoFile poFile, PoEntry entry, AIModelDescriptor aiModelDescriptor, String projectDescription) {
        Logger.info("Assessing translation using AI model {}", aiModelDescriptor.name());
        TranslationServiceAI aiService = AiServices.create(TranslationServiceAI.class, aiModelDescriptor.validationModel());

        return aiService.assess(Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH),
                                poFile.locale().get().getDisplayLanguage(Locale.ENGLISH),
                                projectDescription,
                                entry.untranslatedValue().getValue(),
                                entry.translatedValue().getValue());

    }
}
