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

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import javafx.scene.layout.Pane;
import ooo.autopo.model.ai.AIModelDescriptor;
import org.pdfsam.persistence.PreferencesRepository;

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
    public ChatLanguageModel translationModel() {
        if (isUsable()) {
            var temperature = 0.2d;
            var temperatureIntValue = repo.getInt(GeminiAIPersistentProperty.TEMPERATURE.key(), -1);
            if (temperatureIntValue >= 0) {
                temperature = Math.round(temperatureIntValue / 10.0 * 10) / 10.0;
            }
            return GoogleAiGeminiChatModel.builder()
                    .apiKey(repo.getString(GeminiAIPersistentProperty.API_KEY.key(), ""))
                    .temperature(temperature)
                    .logRequestsAndResponses(true)
                    .modelName(repo.getString(GeminiAIPersistentProperty.MODEL_NAME.key(), "gemini-2.0-flash"))
                    .build();
        }
        return null;
    }

    @Override
    public ChatLanguageModel validationModel() {
        if (isUsable()) {
            var temperature = 0.2d;
            var temperatureIntValue = repo.getInt(GeminiAIPersistentProperty.TEMPERATURE.key(), -1);
            if (temperatureIntValue >= 0) {
                temperature = Math.round(temperatureIntValue / 10.0 * 10) / 10.0;
            }
            return GoogleAiGeminiChatModel.builder()
                    .apiKey(repo.getString(GeminiAIPersistentProperty.API_KEY.key(), ""))
                    .temperature(temperature)
                    .responseFormat(ResponseFormat.JSON)
                    .logRequestsAndResponses(true)
                    .modelName(repo.getString(GeminiAIPersistentProperty.MODEL_NAME.key(), "gemini-2.0-flash"))
                    .build();
        }
        return null;
    }

    @Override
    public boolean isUsable() {
        return isNotBlank(repo.getString(GeminiAIPersistentProperty.API_KEY.key(), ""));
    }
}
