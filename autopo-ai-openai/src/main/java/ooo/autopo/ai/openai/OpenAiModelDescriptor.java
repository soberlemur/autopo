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
import ooo.autopo.model.ai.AiModelDescriptor;
import org.pdfsam.persistence.PreferencesRepository;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Andrea Vacondio
 */
public class OpenAiModelDescriptor implements AiModelDescriptor {
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
            return OpenAiChatModel.builder()
                    .apiKey(repo.getString(OpenAIPersistentProperty.API_KEY.key(), ""))
                    .modelName(OpenAiChatModelName.valueOf(repo.getString(OpenAIPersistentProperty.MODEL_NAME.key(), "")))
                    .build();
        }
        return null;
    }

    @Override
    public boolean isUsable() {
        return isNotBlank(repo.getString(OpenAIPersistentProperty.MODEL_NAME.key(),
                                         (String) null)) && isNotBlank(repo.getString(OpenAIPersistentProperty.API_KEY.key(), (String) null));
    }
}
