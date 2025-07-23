package ooo.autopo.ai.anthropic;

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

import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.anthropic.AnthropicChatModelName;
import dev.langchain4j.model.chat.ChatModel;
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
    public ChatModel translationModel() {
        if (isUsable()) {
            var temperature = 0.2d;
            var temperatureIntValue = repo.getInt(AnthropicAIPersistentProperty.TEMPERATURE.key(), -1);
            if (temperatureIntValue >= 0) {
                temperature = Math.round(temperatureIntValue / 10.0 * 10) / 10.0;
            }
            return AnthropicChatModel.builder()
                    .apiKey(repo.getString(AnthropicAIPersistentProperty.API_KEY.key(), ""))
                    .modelName(repo.getString(AnthropicAIPersistentProperty.MODEL_NAME.key(), "claude-3-7-sonnet-latest"))
                    .temperature(temperature)
                    .logRequests(true)
                    .logResponses(true)
                    .build();

        }
        return null;
    }

    @Override
    public ChatModel validationModel() {
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
