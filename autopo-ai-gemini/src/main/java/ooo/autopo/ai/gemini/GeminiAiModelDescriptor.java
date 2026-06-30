package ooo.autopo.ai.gemini;

/*
 * This file is part of the Autopo project
 * Created 17/03/25
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

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.output.JsonSchemas;
import javafx.scene.layout.Pane;
import ooo.autopo.model.ai.AIModelDescriptor;
import ooo.autopo.model.ai.TranslationAssessment;
import org.pdfsam.persistence.PreferencesRepository;

import static dev.langchain4j.model.chat.request.ResponseFormatType.JSON;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Andrea Vacondio
 */
public class GeminiAiModelDescriptor implements AIModelDescriptor {
    static final String MODEL_ID = "GEMINI";
    private final PreferencesRepository repo = new PreferencesRepository("/ooo/autopo/ai/settings/gemini");

    @Override
    public String id() {
        return MODEL_ID;
    }

    @Override
    public String name() {
        return "Google Gemini";
    }

    @Override
    public Pane settingsPane() {
        return new GeminiAISettings(repo);
    }

    @Override
    public ChatModel translationModel() {
        if (isUsable()) {
            var builder = GoogleAiGeminiChatModel.builder()
                    .apiKey(repo.getString(GeminiAIPersistentProperty.API_KEY.key(), ""))
                    .logRequestsAndResponses(true)
                    .modelName(repo.getString(GeminiAIPersistentProperty.MODEL_NAME.key(), "gemini-3.5-flash"));
            if (repo.getBoolean(GeminiAIPersistentProperty.ENABLE_TEMPERATURE.key(), false)) {
                var temperature = 0.2d;
                var temperatureIntValue = repo.getInt(GeminiAIPersistentProperty.TEMPERATURE.key(), -1);
                if (temperatureIntValue >= 0) {
                    temperature = Math.round(temperatureIntValue / 10.0 * 10) / 10.0;
                }
                builder.temperature(temperature);
            }
            return builder.build();
        }
        return null;
    }

    @Override
    public ChatModel validationModel() {
        if (isUsable()) {
            var responseFormat = ResponseFormat.builder().type(JSON).jsonSchema(JsonSchemas.jsonSchemaFrom(TranslationAssessment.class).get()).build();
            var builder = GoogleAiGeminiChatModel.builder()
                    .apiKey(repo.getString(GeminiAIPersistentProperty.API_KEY.key(), ""))
                    .responseFormat(responseFormat)
                    .logRequestsAndResponses(true)
                    .modelName(repo.getString(GeminiAIPersistentProperty.MODEL_NAME.key(), "gemini-3.5-flash"));
            if (repo.getBoolean(GeminiAIPersistentProperty.ENABLE_TEMPERATURE.key(), false)) {
                var temperature = 0.2d;
                var temperatureIntValue = repo.getInt(GeminiAIPersistentProperty.TEMPERATURE.key(), -1);
                if (temperatureIntValue >= 0) {
                    temperature = Math.round(temperatureIntValue / 10.0 * 10) / 10.0;
                }
                builder.temperature(temperature);
            }
            return builder.build();
        }
        return null;
    }

    @Override
    public boolean isUsable() {
        return isNotBlank(repo.getString(GeminiAIPersistentProperty.API_KEY.key(), ""));
    }
}
