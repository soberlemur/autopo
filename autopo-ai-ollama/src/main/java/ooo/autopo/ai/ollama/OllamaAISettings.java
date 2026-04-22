package ooo.autopo.ai.ollama;

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

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.javafx.FontIcon;
import org.pdfsam.persistence.PreferencesRepository;

import static java.util.Optional.ofNullable;
import static ooo.autopo.i18n.I18nContext.i18n;
import static ooo.autopo.model.ui.Views.helpIcon;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Andrea Vacondio
 */
public class OllamaAISettings extends GridPane {

    public OllamaAISettings(PreferencesRepository repo) {
        this.getStyleClass().addAll("ai-tab", "settings-panel");
        add(new Label(i18n().tr("Model:")), 0, 0);
        var modelCombo = new ComboBox<String>();
        modelCombo.setEditable(true);
        modelCombo.setId("ollamaAiModelCombo");
        modelCombo.getItems().addAll("llama3.3", "llama3.2", "llama3.2:1b", "llama3.1", "mistral");
        modelCombo.setMaxWidth(Double.POSITIVE_INFINITY);
        modelCombo.valueProperty().subscribe((o, n) -> {
            if (isNotBlank(n)) {
                repo.saveString(OllamaAIPersistentProperty.MODEL_NAME.key(), n);
            } else if (isNotBlank(o)) {
                Platform.runLater(() -> modelCombo.setValue(o));
            }
        });
        modelCombo.getEditor().focusedProperty().subscribe((o, focused) -> {
            if (!focused) {
                var text = modelCombo.getEditor().getText();
                if (isBlank(text)) {
                    var currentValue = modelCombo.getValue();
                    if (isNotBlank(currentValue)) {
                        modelCombo.getEditor().setText(currentValue);
                    }
                }
            }
        });
        ofNullable(repo.getString(OllamaAIPersistentProperty.MODEL_NAME.key(), (String) null)).ifPresent(modelCombo::setValue);
        setFillWidth(modelCombo, true);
        add(modelCombo, 1, 0);
        add(helpIcon(i18n().tr("AI Model to use")), 2, 0);

        add(new Label(i18n().tr("Base URL:")), 0, 1);
        var urlField = new TextField();
        urlField.setPromptText("http://localhost:11434");
        ofNullable(repo.getString(OllamaAIPersistentProperty.BASE_URL.key(), (String) null)).ifPresent(urlField::setText);
        urlField.textProperty().subscribe((o, n) -> repo.saveString(OllamaAIPersistentProperty.BASE_URL.key(), n));
        setFillWidth(urlField, true);
        add(urlField, 1, 1, 2, 1);

        add(new Label(i18n().tr("Temperature:")), 0, 2);
        var temperature = new Spinner<Double>(0.0, 2.0, 0.2, 0.1);
        temperature.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        var temperatureValue = repo.getInt(OllamaAIPersistentProperty.TEMPERATURE.key(), -1);
        if (temperatureValue >= 0) {
            temperature.getValueFactory().setValue(Math.round(temperatureValue / 10.0 * 10) / 10.0);
        }
        temperature.valueProperty().subscribe((o, n) -> repo.saveInt(OllamaAIPersistentProperty.TEMPERATURE.key(), (int) (n * 10)));
        add(temperature, 1, 2);
        add(helpIcon(i18n().tr("Higher values make the output more random, lower values make it more deterministic")), 2, 2);

        Button clearButton = new Button(i18n().tr("Clear"));
        clearButton.setTooltip(new Tooltip(i18n().tr("Clear Ollama settings")));
        clearButton.setGraphic(FontIcon.of(FluentUiFilledAL.ERASER_24));
        clearButton.setOnAction(e -> {
            repo.clean();
            urlField.setText("");
            modelCombo.getSelectionModel().clearSelection();
            modelCombo.getEditor().clear();
        });
        add(clearButton, 0, 3);

    }
}
