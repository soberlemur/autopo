package ooo.autopo.ai.openai;

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
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import javafx.scene.layout.Pane;
import ooo.autopo.model.ai.AIModelDescriptor;
import org.pdfsam.persistence.PreferencesRepository;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Andrea Vacondio
 */
public class OpenAiModelDescriptor implements AIModelDescriptor {
    static final String MODEL_ID = "OPENAI";
    private final PreferencesRepository repo = new PreferencesRepository("/ooo/autopo/ai/settings/openai");

    @Override
    public String id() {
        return MODEL_ID;
    }

    @Override
    public String name() {
        return "OpenAI";
    }

    @Override
    public Pane settingsPane() {
        return new OpenAISettings(repo);
    }

    @Override
    public ChatLanguageModel model() {
        if (isUsable()) {
            var temperature = 0.2d;
            var temperatureIntValue = repo.getInt(OpenAIPersistentProperty.TEMPERATURE.key(), -1);
            if (temperatureIntValue >= 0) {
                temperature = Math.round(temperatureIntValue / 10.0 * 10) / 10.0;
            }
            return OpenAiChatModel.builder()
                    .apiKey(repo.getString(OpenAIPersistentProperty.API_KEY.key(), ""))
                    .temperature(temperature)
                    .logRequests(true)
                    .modelName(OpenAiChatModelName.valueOf(repo.getString(OpenAIPersistentProperty.MODEL_NAME.key(), GPT_4.name())))
                    .build();
        }
        return null;
    }

    @Override
    public boolean isUsable() {
        return isNotBlank(repo.getString(OpenAIPersistentProperty.API_KEY.key(), ""));
    }
}
