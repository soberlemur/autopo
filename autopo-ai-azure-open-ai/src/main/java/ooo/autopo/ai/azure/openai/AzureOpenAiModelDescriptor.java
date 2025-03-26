package ooo.autopo.ai.azure.openai;

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

import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import dev.langchain4j.model.azure.AzureOpenAiChatModelName;
import dev.langchain4j.model.chat.ChatLanguageModel;
import javafx.scene.layout.Pane;
import ooo.autopo.model.ai.AIModelDescriptor;
import org.pdfsam.persistence.PreferencesRepository;

import static dev.langchain4j.model.azure.AzureOpenAiChatModelName.GPT_4;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Andrea Vacondio
 */
public class AzureOpenAiModelDescriptor implements AIModelDescriptor {
    static final String MODEL_ID = "AZURE_OPEN_AI";
    private final PreferencesRepository repo = new PreferencesRepository("/ooo/autopo/ai/settings/azure/openai");

    @Override
    public String id() {
        return MODEL_ID;
    }

    @Override
    public String name() {
        return "Azure Open AI";
    }

    @Override
    public Pane settingsPane() {
        return new AzureOpenAISettings(repo);
    }

    @Override
    public ChatLanguageModel translationModel() {
        if (isUsable()) {
            var temperature = 0.2d;
            var temperatureIntValue = repo.getInt(AzureOpenAIPersistentProperty.TEMPERATURE.key(), -1);
            if (temperatureIntValue >= 0) {
                temperature = Math.round(temperatureIntValue / 10.0 * 10) / 10.0;
            }
            return AzureOpenAiChatModel.builder()
                    .apiKey(repo.getString(AzureOpenAIPersistentProperty.API_KEY.key(), ""))
                    .temperature(temperature)
                    .logRequestsAndResponses(true)
                    .deploymentName(AzureOpenAiChatModelName.valueOf(repo.getString(AzureOpenAIPersistentProperty.MODEL_NAME.key(), GPT_4.name())).modelName())
                    .build();
        }
        return null;
    }

    @Override
    public ChatLanguageModel validationModel() {
        if (isUsable()) {
            var temperature = 0.2d;
            var temperatureIntValue = repo.getInt(AzureOpenAIPersistentProperty.TEMPERATURE.key(), -1);
            if (temperatureIntValue >= 0) {
                temperature = Math.round(temperatureIntValue / 10.0 * 10) / 10.0;
            }
            return AzureOpenAiChatModel.builder()
                    .apiKey(repo.getString(AzureOpenAIPersistentProperty.API_KEY.key(), ""))
                    .temperature(temperature)
                    .strictJsonSchema(true)
                    .logRequestsAndResponses(true)
                    .deploymentName(AzureOpenAiChatModelName.valueOf(repo.getString(AzureOpenAIPersistentProperty.MODEL_NAME.key(), GPT_4.name())).modelName())
                    .build();
        }
        return null;
    }

    @Override
    public boolean isUsable() {
        return isNotBlank(repo.getString(AzureOpenAIPersistentProperty.API_KEY.key(), ""));
    }
}
