package ooo.autopo.ai.gemini;

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
    public ChatLanguageModel model() {
        if (isUsable()) {
            return GoogleAiGeminiChatModel.builder()
                    .apiKey(repo.getString(GeminiAIPersistentProperty.API_KEY.key(), ""))
                    .temperature(0.2)
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
