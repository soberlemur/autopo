/*
 * This file is part of the PDF Split And Merge source code
 * Created on 29/ott/2013
 * Copyright 2017 by Sober Lemur S.r.l. (info@soberlemur.com).
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
package ooo.autopo.app.ui.settings;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import ooo.autopo.app.context.IntegerPersistentProperty;
import ooo.autopo.app.ui.Style;
import ooo.autopo.app.ui.components.preferences.PreferenceComboBox;
import ooo.autopo.i18n.SetLocaleRequest;
import ooo.autopo.model.ui.ComboItem;

import java.util.Comparator;
import java.util.Locale;

import static ooo.autopo.app.context.ApplicationContext.app;
import static ooo.autopo.i18n.I18nContext.i18n;
import static ooo.autopo.model.ui.Views.helpIcon;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * Preference pane displaying the appearance section
 *
 * @author Andrea Vacondio
 */
class GeneralSettingsPane extends GridPane {

    @Inject
    public GeneralSettingsPane(@Named("localeCombo") PreferenceComboBox<ComboItem<String>> localeCombo,
            @Named("themeCombo") PreferenceComboBox<ComboItem<String>> themeCombo, @Named("fontSizeCombo") PreferenceComboBox<ComboItem<String>> fontSizeCombo,
            @Named("defaultAiCombo") PreferenceComboBox<ComboItem<String>> defaultAiCombo,
            @Named("validationAiCombo") PreferenceComboBox<ComboItem<String>> validationAiCombo) {
        this.getStyleClass().add("settings-panel");
        add(new Label(i18n().tr("Language:")), 0, 0);
        i18n().getSupported().stream().map(ComboItem::fromLocale).sorted(Comparator.comparing(ComboItem::description)).forEach(localeCombo.getItems()::add);

        localeCombo.setValue(ComboItem.fromLocale(Locale.getDefault()));
        localeCombo.valueProperty().addListener((observable, oldValue, newValue) -> eventStudio().broadcast(new SetLocaleRequest(newValue.key())));
        localeCombo.setMaxWidth(Double.POSITIVE_INFINITY);
        setFillWidth(localeCombo, true);
        add(localeCombo, 1, 0);
        add(helpIcon(i18n().tr("Set your preferred language (restart needed)")), 2, 0);

        add(new Label(i18n().tr("Theme:")), 0, 1);
        themeCombo.setMaxWidth(Double.POSITIVE_INFINITY);
        setFillWidth(themeCombo, true);
        add(themeCombo, 1, 1);
        add(helpIcon(i18n().tr("Set the application theme")), 2, 1);

        add(new Label(i18n().tr("Font size:")), 0, 2);
        fontSizeCombo.setMaxWidth(Double.POSITIVE_INFINITY);
        setFillWidth(fontSizeCombo, true);
        add(fontSizeCombo, 1, 2);
        add(helpIcon(i18n().tr("Set the application font size")), 2, 2);

        add(new Label(i18n().tr("Translations AI provider:")), 0, 3);
        defaultAiCombo.setMaxWidth(Double.POSITIVE_INFINITY);
        setFillWidth(defaultAiCombo, true);
        add(defaultAiCombo, 1, 3);
        add(helpIcon(i18n().tr("Set the AI provider for translations")), 2, 3);

        add(new Label(i18n().tr("Validations AI provider:")), 0, 4);
        validationAiCombo.setMaxWidth(Double.POSITIVE_INFINITY);
        setFillWidth(validationAiCombo, true);
        add(validationAiCombo, 1, 4);
        add(helpIcon(i18n().tr("Set the AI provider used for validation")), 2, 4);

        add(new Label(i18n().tr("Batch size:")), 0, 5);
        var batchSize = new Spinner<Integer>(0, Integer.MAX_VALUE, 50, 1);
        batchSize.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        batchSize.getValueFactory().setValue(app().persistentSettings().get(IntegerPersistentProperty.BATCH_SIZE));
        batchSize.valueProperty().subscribe((o, n) -> app().persistentSettings().set(IntegerPersistentProperty.BATCH_SIZE, n));
        add(batchSize, 1, 5);
        add(helpIcon(i18n().tr("Set the number of entries to translate with the AI batch translation")), 2, 5);

        add(helpIcon(i18n().tr("Higher values make the output more random, lower values make it more deterministic")), 2, 2);
        getStyleClass().addAll(Style.CONTAINER.css());
        getStyleClass().addAll(Style.GRID.css());
    }

}
