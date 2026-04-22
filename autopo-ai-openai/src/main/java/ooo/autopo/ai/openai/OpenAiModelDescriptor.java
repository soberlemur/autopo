package ooo.autopo.ai.openai;

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
import dev.langchain4j.model.openai.OpenAiChatModel;
import javafx.scene.layout.Pane;
import ooo.autopo.model.ai.AIModelDescriptor;
import org.pdfsam.persistence.PreferencesRepository;

import java.util.Set;

import static dev.langchain4j.model.chat.Capability.RESPONSE_FORMAT_JSON_SCHEMA;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_5_1;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Andrea Vacondio
 */
public class OpenAiModelDescriptor implements AIModelDescriptor {
    static final String MODEL_ID = "OPENAI";
    public static final String DEFAULT_OPENAI_URL = "https://api.openai.com/v1";
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
    public ChatModel translationModel() {
        if (isUsable()) {
            return builder().logRequests(true).modelName(repo.getString(OpenAIPersistentProperty.MODEL_NAME.key(), GPT_5_1.toString()))
                    .build();
        }
        return null;
    }

    @Override
    public ChatModel validationModel() {
        if (isUsable()) {
            return builder().supportedCapabilities(Set.of(RESPONSE_FORMAT_JSON_SCHEMA)) // see [2] below
                            .strictJsonSchema(true).logRequests(true).modelName(repo.getString(OpenAIPersistentProperty.MODEL_NAME.key(), GPT_5_1.toString()))
                    .build();
        }
        return null;
    }

    private OpenAiChatModel.OpenAiChatModelBuilder builder() {
        var temperature = 0.2d;
        var temperatureIntValue = repo.getInt(OpenAIPersistentProperty.TEMPERATURE.key(), -1);
        if (temperatureIntValue >= 0) {
            temperature = Math.round(temperatureIntValue / 10.0 * 10) / 10.0;
        }
        return OpenAiChatModel.builder()
                .baseUrl(defaultIfBlank(repo.getString(OpenAIPersistentProperty.BASE_URL.key(), DEFAULT_OPENAI_URL), DEFAULT_OPENAI_URL))
                .apiKey(repo.getString(OpenAIPersistentProperty.API_KEY.key(), ""))
                .temperature(temperature);
    }

    @Override
    public boolean isUsable() {
        return isNotBlank(repo.getString(OpenAIPersistentProperty.API_KEY.key(), ""));
    }
}
