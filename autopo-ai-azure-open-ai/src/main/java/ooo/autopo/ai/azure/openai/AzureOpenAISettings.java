package ooo.autopo.ai.azure.openai;

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

import dev.langchain4j.model.azure.AzureOpenAiChatModelName;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import ooo.autopo.model.ui.ApiKeyTextField;
import ooo.autopo.model.ui.ComboItem;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.javafx.FontIcon;
import org.pdfsam.persistence.PreferencesRepository;

import static dev.langchain4j.model.azure.AzureOpenAiChatModelName.GPT_3_5_TURBO;
import static dev.langchain4j.model.azure.AzureOpenAiChatModelName.GPT_3_5_TURBO_16K;
import static dev.langchain4j.model.azure.AzureOpenAiChatModelName.GPT_4;
import static dev.langchain4j.model.azure.AzureOpenAiChatModelName.GPT_4_32K;
import static dev.langchain4j.model.azure.AzureOpenAiChatModelName.GPT_4_O;
import static dev.langchain4j.model.azure.AzureOpenAiChatModelName.GPT_4_TURBO;
import static java.util.Optional.ofNullable;
import static ooo.autopo.i18n.I18nContext.i18n;
import static ooo.autopo.model.ui.Views.helpIcon;

/**
 * @author Andrea Vacondio
 */
public class AzureOpenAISettings extends GridPane {

    public AzureOpenAISettings(PreferencesRepository repo) {
        this.getStyleClass().addAll("ai-tab", "settings-panel");
        add(new Label(i18n().tr("Model:")), 0, 0);
        var modelCombo = new ComboBox<ComboItem<AzureOpenAiChatModelName>>();
        modelCombo.setId("azureOpenAiModelCombo");
        modelCombo.getItems().add(new ComboItem<>(GPT_3_5_TURBO, GPT_3_5_TURBO.name()));
        modelCombo.getItems().add(new ComboItem<>(GPT_3_5_TURBO_16K, GPT_3_5_TURBO_16K.name()));
        modelCombo.getItems().add(new ComboItem<>(GPT_4, GPT_4.name()));
        modelCombo.getItems().add(new ComboItem<>(GPT_4_TURBO, GPT_4_TURBO.name()));
        modelCombo.getItems().add(new ComboItem<>(GPT_4_32K, GPT_4_32K.name()));
        modelCombo.getItems().add(new ComboItem<>(GPT_4_O, GPT_4_O.name()));

        modelCombo.setMaxWidth(Double.POSITIVE_INFINITY);
        modelCombo.valueProperty().subscribe((o, n) -> repo.saveString(AzureOpenAIPersistentProperty.MODEL_NAME.key(), n.key().name()));
        ofNullable(repo.getString(AzureOpenAIPersistentProperty.MODEL_NAME.key(), (String) null)).map(AzureOpenAiChatModelName::valueOf)
                                                                                                 .map(m -> new ComboItem<>(m, m.name()))
                                                                                                 .ifPresent(modelCombo::setValue);
        setFillWidth(modelCombo, true);
        add(modelCombo, 1, 0);
        add(helpIcon(i18n().tr("AI Model to use")), 2, 0);

        add(new Label(i18n().tr("API key:")), 0, 1);
        var apiField = new ApiKeyTextField();
        ofNullable(repo.getString(AzureOpenAIPersistentProperty.API_KEY.key(), (String) null)).ifPresent(apiField::setText);
        apiField.passwordProperty().subscribe((o, n) -> repo.saveString(AzureOpenAIPersistentProperty.API_KEY.key(), n));
        setFillWidth(apiField, true);
        add(apiField, 1, 1, 2, 1);

        add(new Label(i18n().tr("Temperature:")), 0, 2);
        var temperature = new Spinner<Double>(0.0, 2.0, 0.2, 0.1);
        temperature.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        var temperatureValue = repo.getInt(AzureOpenAIPersistentProperty.TEMPERATURE.key(), -1);
        if (temperatureValue >= 0) {
            temperature.getValueFactory().setValue(Math.round(temperatureValue / 10.0 * 10) / 10.0);
        }
        temperature.valueProperty().subscribe((o, n) -> repo.saveInt(AzureOpenAIPersistentProperty.TEMPERATURE.key(), (int) (n * 10)));
        add(temperature, 1, 2);
        add(helpIcon(i18n().tr("Higher values make the output more random, lower values make it more deterministic")), 2, 2);

        Button clearButton = new Button(i18n().tr("Clear"));
        clearButton.setTooltip(new Tooltip(i18n().tr("Clear Azure Open AI settings")));
        clearButton.setGraphic(FontIcon.of(FluentUiFilledAL.ERASER_24));
        clearButton.setOnAction(e -> {
            repo.clean();
            apiField.setText("");
            modelCombo.getSelectionModel().clearSelection();
        });
        add(clearButton, 0, 3);

    }
}
