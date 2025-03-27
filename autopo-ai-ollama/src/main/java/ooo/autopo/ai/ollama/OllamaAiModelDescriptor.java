package ooo.autopo.ai.ollama;

/*
 * This file is part of the Autopo project
 * Created 17/03/25
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

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import javafx.scene.layout.Pane;
import ooo.autopo.model.ai.AIModelDescriptor;
import org.pdfsam.persistence.PreferencesRepository;

import static dev.langchain4j.model.chat.request.ResponseFormat.JSON;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Andrea Vacondio
 */
public class OllamaAiModelDescriptor implements AIModelDescriptor {
    static final String MODEL_ID = "OLLAMA";
    private final PreferencesRepository repo = new PreferencesRepository("/ooo/autopo/ai/settings/ollama");

    @Override
    public String id() {
        return MODEL_ID;
    }

    @Override
    public String name() {
        return "Ollama";
    }

    @Override
    public Pane settingsPane() {
        return new OllamaAISettings(repo);
    }

    @Override
    public ChatLanguageModel translationModel() {
        if (isUsable()) {
            var temperature = 0.2d;
            var temperatureIntValue = repo.getInt(OllamaAIPersistentProperty.TEMPERATURE.key(), -1);
            if (temperatureIntValue >= 0) {
                temperature = Math.round(temperatureIntValue / 10.0 * 10) / 10.0;
            }
            return OllamaChatModel.builder()
                    .baseUrl(repo.getString(OllamaAIPersistentProperty.BASE_URL.key(), ""))
                    .modelName(repo.getString(OllamaAIPersistentProperty.MODEL_NAME.key(), "llama3.2"))
                    .logRequests(true)
                    .logResponses(true)
                    .temperature(temperature)
                    .build();
        }
        return null;
    }

    @Override
    public ChatLanguageModel validationModel() {
        if (isUsable()) {
            var temperature = 0.2d;
            var temperatureIntValue = repo.getInt(OllamaAIPersistentProperty.TEMPERATURE.key(), -1);
            if (temperatureIntValue >= 0) {
                temperature = Math.round(temperatureIntValue / 10.0 * 10) / 10.0;
            }
            return OllamaChatModel.builder()
                    .baseUrl(repo.getString(OllamaAIPersistentProperty.BASE_URL.key(), ""))
                    .modelName(repo.getString(OllamaAIPersistentProperty.MODEL_NAME.key(), "llama3.2"))
                    .responseFormat(JSON)
                    .logRequests(true)
                    .logResponses(true)
                    .temperature(temperature)
                    .build();
        }
        return null;
    }

    @Override
    public boolean isUsable() {
        return isNotBlank(repo.getString(OllamaAIPersistentProperty.BASE_URL.key(), ""));
    }
}
