package ooo.autopo.ai.gemini;

/*
 * This file is part of the Autopo project
 * Created 18/03/25
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

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import ooo.autopo.model.ui.ApiKeyTextField;
import ooo.autopo.model.ui.ComboItem;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.javafx.FontIcon;
import org.pdfsam.persistence.PreferencesRepository;

import static java.util.Optional.ofNullable;
import static ooo.autopo.i18n.I18nContext.i18n;
import static ooo.autopo.model.ui.Views.helpIcon;

/**
 * @author Andrea Vacondio
 */
public class GeminiAISettings extends GridPane {

    public GeminiAISettings(PreferencesRepository repo) {
        this.getStyleClass().addAll("ai-tab", "settings-panel");
        add(new Label(i18n().tr("Model:")), 0, 0);
        var modelCombo = new ComboBox<ComboItem<String>>();
        modelCombo.setId("openAiModelCombo");
        modelCombo.getItems().add(new ComboItem<>("gemini-2.0-flash", "Gemini 2.0 Flash"));
        modelCombo.getItems().add(new ComboItem<>("gemini-1.5-flash", "Gemini 1.5 Flash"));
        modelCombo.getItems().add(new ComboItem<>("gemini-1.5-pro", "Gemini 1.5 Pro"));

        modelCombo.setMaxWidth(Double.POSITIVE_INFINITY);
        modelCombo.valueProperty().subscribe((o, n) -> repo.saveString(GeminiAIPersistentProperty.MODEL_NAME.key(), n.key()));
        ofNullable(repo.getString(GeminiAIPersistentProperty.MODEL_NAME.key(), (String) null))
                .map(m -> new ComboItem<>(m, ""))
                .ifPresent(modelCombo::setValue);
        setFillWidth(modelCombo, true);
        add(modelCombo, 1, 0);
        add(helpIcon(i18n().tr("AI Model to use")), 2, 0);

        add(new Label(i18n().tr("API key:")), 0, 1);
        var apiField = new ApiKeyTextField();
        ofNullable(repo.getString(GeminiAIPersistentProperty.API_KEY.key(), (String) null)).ifPresent(apiField::setText);
        apiField.textProperty().subscribe((o, n) -> repo.saveString(GeminiAIPersistentProperty.API_KEY.key(), n));
        setFillWidth(apiField, true);
        add(apiField, 1, 1, 2, 1);

        Button clearButton = new Button(i18n().tr("Clear"));
        clearButton.setTooltip(new Tooltip(i18n().tr("Clear Google Gemini settings")));
        clearButton.setGraphic(FontIcon.of(FluentUiFilledAL.ERASER_24));
        clearButton.setOnAction(e -> {
            repo.clean();
            apiField.setText("");
            modelCombo.getSelectionModel().clearSelection();
        });
        add(clearButton, 0, 2);

    }
}
