package ooo.autopo.app.config;

/*
 * This file is part of the Autopo project
 * Created 18/02/25
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

import jakarta.inject.Named;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.util.Subscription;
import ooo.autopo.app.context.StringPersistentProperty;
import ooo.autopo.app.ui.Themes;
import ooo.autopo.app.ui.components.preferences.PreferenceComboBox;
import ooo.autopo.model.ai.AIModelDescriptor;
import ooo.autopo.model.ui.ComboItem;
import org.pdfsam.injector.Provides;

import java.util.Comparator;
import java.util.stream.IntStream;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ooo.autopo.app.context.ApplicationContext.app;
import static ooo.autopo.app.context.StringPersistentProperty.AI_MODEL;
import static ooo.autopo.app.context.StringPersistentProperty.FONT_SIZE;
import static ooo.autopo.app.context.StringPersistentProperty.THEME;
import static ooo.autopo.app.context.StringPersistentProperty.VALIDATION_AI_MODEL;
import static ooo.autopo.i18n.I18nContext.i18n;
import static ooo.autopo.model.ui.ComboItem.keyWithEmptyValue;

/**
 * @author Andrea Vacondio
 */
public class PreferencesConfig {

    @Provides
    @Named("localeCombo")
    public PreferenceComboBox<ComboItem<String>> localeCombo() {
        return new PreferenceComboBox<>(StringPersistentProperty.LOCALE);
    }

    @Provides
    @Named("themeCombo")
    public PreferenceComboBox<ComboItem<String>> themeCombo() {
        PreferenceComboBox<ComboItem<String>> themeCombo = new PreferenceComboBox<>(THEME);
        themeCombo.setId("themeCombo");
        Themes.themes()
              .entrySet()
              .stream()
              .sorted(Comparator.comparing(e -> e.getValue().name()))
              .map(entry -> new ComboItem<>(entry.getKey(), entry.getValue().name()))
              .forEach(themeCombo.getItems()::add);
        final Subscription[] subscription = new Subscription[1];
        subscription[0] = app().runtimeState().theme().subscribe(t -> {
            if (nonNull(t)) {
                themeCombo.setValue(new ComboItem<>(t.id(), t.name()));
                themeCombo.valueProperty()
                          .addListener((observable, oldVal, newVal) -> ofNullable(Themes.get(newVal.key())).ifPresent(theme -> app().runtimeState()
                                                                                                                                    .theme(theme)));
                ofNullable(subscription[0]).ifPresent(Subscription::unsubscribe);
            }
        });
        return themeCombo;
    }

    @Provides
    @Named("fontSizeCombo")
    public PreferenceComboBox<ComboItem<String>> fontSizeCombo() {
        PreferenceComboBox<ComboItem<String>> fontSizeCombo = new PreferenceComboBox<>(FONT_SIZE);
        fontSizeCombo.setId("fontSizeCombo");
        fontSizeCombo.getItems().add(new ComboItem<>("", i18n().tr("System default")));
        IntStream.range(9, 22).forEach(i -> fontSizeCombo.getItems().add(new ComboItem<>(i + "px", i + "px")));
        fontSizeCombo.setValue(keyWithEmptyValue(app().persistentSettings().get(FONT_SIZE).orElse("")));
        return fontSizeCombo;
    }

    @Provides
    @Named("defaultAiCombo")
    public PreferenceComboBox<ComboItem<String>> defaultAiCombo() {
        PreferenceComboBox<ComboItem<String>> defaultAiCombo = new PreferenceComboBox<>(AI_MODEL);
        defaultAiCombo.setId("defaultAiCombo");
        defaultAiCombo.getItems().add(new ComboItem<>("", i18n().tr("First available")));
        app().runtimeState()
             .aiModels()
             .stream()
             .sorted(Comparator.comparing(AIModelDescriptor::name))
             .map(d -> new ComboItem<>(d.id(), d.name()))
             .forEachOrdered(defaultAiCombo.getItems()::add);
        defaultAiCombo.setValue(keyWithEmptyValue(app().persistentSettings().get(AI_MODEL).orElse("")));
        return defaultAiCombo;
    }

    @Provides
    @Named("validationAiCombo")
    public PreferenceComboBox<ComboItem<String>> validationAiCombo() {
        PreferenceComboBox<ComboItem<String>> validationAiCombo = new PreferenceComboBox<>(VALIDATION_AI_MODEL);
        validationAiCombo.setId("validationAiCombo");
        validationAiCombo.getItems().add(new ComboItem<>("", i18n().tr("First available")));
        app().runtimeState()
             .aiModels()
             .stream()
             .sorted(Comparator.comparing(AIModelDescriptor::name))
             .map(d -> new ComboItem<>(d.id(), d.name()))
             .forEachOrdered(validationAiCombo.getItems()::add);
        validationAiCombo.setValue(keyWithEmptyValue(app().persistentSettings().get(VALIDATION_AI_MODEL).orElse("")));
        return validationAiCombo;
    }

    @Provides
    @Named("aiSettings")
    public TabPane aiSettings() {
        var aiPane = new TabPane();
        aiPane.getStyleClass().add("settings-panel");
        app().runtimeState()
             .aiModels()
             .stream()
             .sorted(Comparator.comparing(AIModelDescriptor::name))
             .map(d -> new Tab(d.name(), d.settingsPane()))
             .forEachOrdered(aiPane.getTabs()::add);
        aiPane.getTabs().forEach(tab -> tab.setClosable(false));
        return aiPane;
    }
}
