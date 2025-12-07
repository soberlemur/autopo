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

import dev.langchain4j.http.client.jdk.JdkHttpClient;
import dev.langchain4j.http.client.jdk.JdkHttpClientBuilder;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import javafx.scene.layout.Pane;
import ooo.autopo.model.ai.AIModelDescriptor;
import org.pdfsam.persistence.PreferencesRepository;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Set;

import static dev.langchain4j.model.chat.Capability.RESPONSE_FORMAT_JSON_SCHEMA;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Andrea Vacondio
 */
public class OpenAiModelDescriptor implements AIModelDescriptor {
    static final String MODEL_ID = "OPENAI";
    private static final String DEFAULT_OPENAI_BASE_URL = "https://api.openai.com/v1/";
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
            var temperature = 0.2d;
            var temperatureIntValue = repo.getInt(OpenAIPersistentProperty.TEMPERATURE.key(), -1);
            if (temperatureIntValue >= 0) {
                temperature = Math.round(temperatureIntValue / 10.0 * 10) / 10.0;
            }

            JdkHttpClientBuilder customHttpClientBuilder = JdkHttpClient.builder()
                    .httpClientBuilder(HttpClient.newBuilder()
                            .version(HttpClient.Version.HTTP_1_1) // Specify HTTP/1.1 for certain local models like vLLM
                            .connectTimeout(Duration.ofSeconds(10))) // Set connection timeout
                    .readTimeout(Duration.ofSeconds(60)); // Set read timeout

            var builder = OpenAiChatModel.builder()
                    .apiKey(repo.getString(OpenAIPersistentProperty.API_KEY.key(), ""))
                    .httpClientBuilder(customHttpClientBuilder)
                    .temperature(temperature)
                    .logRequests(true);

            var baseUrl = repo.getString(OpenAIPersistentProperty.BASE_URL.key(), (String) null);
            if (isNotBlank(baseUrl)) {
                builder.baseUrl(baseUrl);
            } else {
                builder.baseUrl(DEFAULT_OPENAI_BASE_URL);
            }

            var modelName = repo.getString(OpenAIPersistentProperty.MODEL_NAME.key(), GPT_4.name());
            try {
                builder.modelName(OpenAiChatModelName.valueOf(modelName));
            } catch (IllegalArgumentException e) {
                // Custom model name not in enum, use as string
                builder.modelName(modelName);
            }

            return builder.build();
        }
        return null;
    }

    @Override
    public ChatModel validationModel() {
        if (isUsable()) {
            var temperature = 0.2d;
            var temperatureIntValue = repo.getInt(OpenAIPersistentProperty.TEMPERATURE.key(), -1);
            if (temperatureIntValue >= 0) {
                temperature = Math.round(temperatureIntValue / 10.0 * 10) / 10.0;
            }

            JdkHttpClientBuilder customHttpClientBuilder = JdkHttpClient.builder()
                    .httpClientBuilder(HttpClient.newBuilder()
                            .version(HttpClient.Version.HTTP_1_1) // Specify HTTP/1.1 for certain local models like vLLM
                            .connectTimeout(Duration.ofSeconds(10))) // Set connection timeout
                    .readTimeout(Duration.ofSeconds(60)); // Set read timeout

            var builder = OpenAiChatModel.builder()
                    .apiKey(repo.getString(OpenAIPersistentProperty.API_KEY.key(), ""))
                    .httpClientBuilder(customHttpClientBuilder)
                    .temperature(temperature)
                    .supportedCapabilities(Set.of(RESPONSE_FORMAT_JSON_SCHEMA)) // see [2] below
                    .strictJsonSchema(true)
                    .logRequests(true);

            var baseUrl = repo.getString(OpenAIPersistentProperty.BASE_URL.key(), (String) null);
            if (isNotBlank(baseUrl)) {
                builder.baseUrl(baseUrl);
            } else {
                builder.baseUrl(DEFAULT_OPENAI_BASE_URL);
            }

            var modelName = repo.getString(OpenAIPersistentProperty.MODEL_NAME.key(), GPT_4.name());
            try {
                builder.modelName(OpenAiChatModelName.valueOf(modelName));
            } catch (IllegalArgumentException e) {
                // Custom model name not in enum, use as string
                builder.modelName(modelName);
            }

            return builder.build();
        }
        return null;
    }

    @Override
    public boolean isUsable() {
        return isNotBlank(repo.getString(OpenAIPersistentProperty.API_KEY.key(), ""));
    }
}
