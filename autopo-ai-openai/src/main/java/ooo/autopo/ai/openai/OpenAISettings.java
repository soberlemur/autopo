package ooo.autopo.ai.openai;

/*
 * This file is part of the Autopo project
 * Created 18/03/25
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

import dev.langchain4j.model.openai.OpenAiChatModelName;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import ooo.autopo.model.ui.ApiKeyTextField;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.javafx.FontIcon;
import org.pdfsam.persistence.PreferencesRepository;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_3_5_TURBO;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_3_5_TURBO_16K;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_1;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_1_MINI;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_1_NANO;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_32K;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_TURBO;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_5;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_5_MINI;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_5_NANO;
import static dev.langchain4j.model.openai.OpenAiChatModelName.O1;
import static dev.langchain4j.model.openai.OpenAiChatModelName.O1_MINI;
import static dev.langchain4j.model.openai.OpenAiChatModelName.O3_MINI;
import static java.util.Optional.ofNullable;
import static ooo.autopo.i18n.I18nContext.i18n;
import static ooo.autopo.model.ui.Views.helpIcon;

/**
 * @author Andrea Vacondio
 */
public class OpenAISettings extends GridPane {

    public OpenAISettings(PreferencesRepository repo) {
        this.getStyleClass().addAll("ai-tab", "settings-panel");
        add(new Label(i18n().tr("Model:")), 0, 0);
        var modelCombo = new ComboBox<String>();
        modelCombo.setId("openAiModelCombo");
        modelCombo.setEditable(true);
        modelCombo.getItems().add(GPT_5.name());
        modelCombo.getItems().add(GPT_5_MINI.name());
        modelCombo.getItems().add(GPT_5_NANO.name());
        modelCombo.getItems().add(GPT_4_1.name());
        modelCombo.getItems().add(GPT_4_1_MINI.name());
        modelCombo.getItems().add(GPT_4_1_NANO.name());
        modelCombo.getItems().add(GPT_4.name());
        modelCombo.getItems().add(GPT_4_TURBO.name());
        modelCombo.getItems().add(GPT_4_32K.name());
        modelCombo.getItems().add(GPT_4_O.name());
        modelCombo.getItems().add(GPT_3_5_TURBO.name());
        modelCombo.getItems().add(GPT_3_5_TURBO_16K.name());
        modelCombo.getItems().add(O1.name());
        modelCombo.getItems().add(O1_MINI.name());
        modelCombo.getItems().add(O3_MINI.name());

        modelCombo.setMaxWidth(Double.POSITIVE_INFINITY);
        modelCombo.valueProperty().subscribe((o, n) -> {
            if (n != null && !n.isEmpty()) {
                repo.saveString(OpenAIPersistentProperty.MODEL_NAME.key(), n);
            }
        });
        ofNullable(repo.getString(OpenAIPersistentProperty.MODEL_NAME.key(), (String) null))
                .ifPresent(modelCombo::setValue);
        setFillWidth(modelCombo, true);
        add(modelCombo, 1, 0);
        add(helpIcon(i18n().tr("AI Model to use")), 2, 0);

        add(new Label(i18n().tr("API key:")), 0, 1);
        var apiField = new ApiKeyTextField();
        ofNullable(repo.getString(OpenAIPersistentProperty.API_KEY.key(), (String) null)).ifPresent(apiField::setText);
        apiField.passwordProperty().subscribe((o, n) -> repo.saveString(OpenAIPersistentProperty.API_KEY.key(), n));
        setFillWidth(apiField, true);
        add(apiField, 1, 1, 2, 1);

        add(new Label(i18n().tr("Temperature:")), 0, 2);
        var temperature = new Spinner<Double>(0.0, 2.0, 0.2, 0.1);
        temperature.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        var temperatureValue = repo.getInt(OpenAIPersistentProperty.TEMPERATURE.key(), -1);
        if (temperatureValue >= 0) {
            temperature.getValueFactory().setValue(Math.round(temperatureValue / 10.0 * 10) / 10.0);
        }
        temperature.valueProperty().subscribe((o, n) -> repo.saveInt(OpenAIPersistentProperty.TEMPERATURE.key(), (int) (n * 10)));
        add(temperature, 1, 2);
        add(helpIcon(i18n().tr("Higher values make the output more random, lower values make it more deterministic")), 2, 2);

        add(new Label(i18n().tr("Base URL:")), 0, 4);
        var urlField = new TextField();
        urlField.setPromptText("https://api.openai.com/v1/");
        ofNullable(repo.getString(OpenAIPersistentProperty.BASE_URL.key(), (String) null)).ifPresent(urlField::setText);
        urlField.textProperty().subscribe((o, n) -> repo.saveString(OpenAIPersistentProperty.BASE_URL.key(), n));
        setFillWidth(urlField, true);
        add(urlField, 1, 4, 2, 1);

        Button clearButton = new Button(i18n().tr("Clear"));
        clearButton.setTooltip(new Tooltip(i18n().tr("Clear OpenAI settings")));
        clearButton.setGraphic(FontIcon.of(FluentUiFilledAL.ERASER_24));
        clearButton.setOnAction(e -> {
            repo.clean();
            apiField.setText("");
            modelCombo.getSelectionModel().clearSelection();
            urlField.setText("");
        });
        add(clearButton, 0, 3);
    }
}
