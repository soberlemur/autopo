package ooo.autopo.ai.anthropic;

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

import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.anthropic.AnthropicChatModelName;
import dev.langchain4j.model.chat.ChatLanguageModel;
import javafx.scene.layout.Pane;
import ooo.autopo.model.ai.AIModelDescriptor;
import org.pdfsam.persistence.PreferencesRepository;
import org.tinylog.Logger;

import static dev.langchain4j.model.anthropic.AnthropicChatModelName.CLAUDE_3_5_HAIKU_20241022;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Andrea Vacondio
 */
public class AnthropicAiModelDescriptor implements AIModelDescriptor {
    static final String MODEL_ID = "ANTHROPIC";
    private final PreferencesRepository repo = new PreferencesRepository("/ooo/autopo/ai/settings/anthropic");

    @Override
    public String id() {
        return MODEL_ID;
    }

    @Override
    public String name() {
        return "Anthropic";
    }

    @Override
    public Pane settingsPane() {
        return new AnthropicAISettings(repo);
    }

    @Override
    public ChatLanguageModel translationModel() {
        if (isUsable()) {
            var temperature = 0.2d;
            var temperatureIntValue = repo.getInt(AnthropicAIPersistentProperty.TEMPERATURE.key(), -1);
            if (temperatureIntValue >= 0) {
                temperature = Math.round(temperatureIntValue / 10.0 * 10) / 10.0;
            }
            return AnthropicChatModel.builder()
                    .apiKey(repo.getString(AnthropicAIPersistentProperty.API_KEY.key(), ""))
                    .modelName(AnthropicChatModelName.valueOf(repo.getString(AnthropicAIPersistentProperty.MODEL_NAME.key(), CLAUDE_3_5_HAIKU_20241022.name())))
                    .temperature(temperature)
                    .logRequests(true)
                    .logResponses(true)
                    .build();

        }
        return null;
    }

    @Override
    public ChatLanguageModel validationModel() {
        if (isUsable()) {
            Logger.warn("JSON mode not yet implemented for Anthropic AI");
            var temperature = 0.2d;
            var temperatureIntValue = repo.getInt(AnthropicAIPersistentProperty.TEMPERATURE.key(), -1);
            if (temperatureIntValue >= 0) {
                temperature = Math.round(temperatureIntValue / 10.0 * 10) / 10.0;
            }
            return AnthropicChatModel.builder()
                    .apiKey(repo.getString(AnthropicAIPersistentProperty.API_KEY.key(), ""))
                    .modelName(AnthropicChatModelName.valueOf(repo.getString(AnthropicAIPersistentProperty.MODEL_NAME.key(), CLAUDE_3_5_HAIKU_20241022.name())))
                    .temperature(temperature)
                    .logRequests(true)
                    .logResponses(true)
                    .build();

        }
        return null;
    }

    @Override
    public boolean isUsable() {
        return isNotBlank(repo.getString(AnthropicAIPersistentProperty.API_KEY.key(), ""));
    }
}
