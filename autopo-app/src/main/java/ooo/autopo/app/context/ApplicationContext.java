package ooo.autopo.app.context;

/*
 * This file is part of the Autopo project
 * Created 13/02/25
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

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.Scene;
import ooo.autopo.model.ai.AIModelDescriptor;
import ooo.autopo.model.po.PoEntry;
import ooo.autopo.model.po.PoFile;
import ooo.autopo.model.project.Project;
import org.apache.commons.lang3.StringUtils;
import org.pdfsam.injector.Injector;
import org.pdfsam.injector.Key;
import org.pdfsam.persistence.PreferencesRepository;

import java.io.Closeable;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static java.util.function.Predicate.not;
import static ooo.autopo.app.context.StringPersistentProperty.FONT_SIZE;

/**
 * @author Andrea Vacondio
 */
public class ApplicationContext implements Closeable {
    public static final String APPLICATION_TITLE = "Autopo";
    private static final ApplicationContext CONTEXT = new ApplicationContext();

    private final ApplicationPersistentSettings persistentSettings;
    private ApplicationRuntimeState runtimeState;
    private Optional<Injector> injector = Optional.empty();

    private ApplicationContext() {
        this(new ApplicationPersistentSettings(new PreferencesRepository("/ooo/autopo/cleanable/settings")), null);
    }

    /**
     * @deprecated use in tests
     */
    @Deprecated
    ApplicationContext(ApplicationPersistentSettings persistentSettings, ApplicationRuntimeState runtimeState) {
        this.persistentSettings = persistentSettings;
        this.runtimeState = runtimeState;
    }

    /**
     * @return the application context
     */
    public static ApplicationContext app() {
        return CONTEXT;
    }

    /**
     * @return the application settings
     */
    public ApplicationPersistentSettings persistentSettings() {
        return this.persistentSettings;
    }

    /**
     * @return the application runtime state
     */
    public ApplicationRuntimeState runtimeState() {
        synchronized (this) {
            if (Objects.isNull(this.runtimeState)) {
                this.runtimeState = new ApplicationRuntimeState();
            }
        }
        return this.runtimeState;
    }

    /**
     * @return the current project or null if no project has been selected yet
     */
    public Project currentProject() {
        return runtimeState().project().getValue();
    }

    /**
     * @return the current .po file or null if no .po file has been selected yet
     */
    public PoFile currentPoFile() {
        return runtimeState().poFile().getValue();
    }

    /**
     * @return the current entry of the.po file or null if no entry has been selected yet
     */
    public PoEntry currentPoEntry() {
        return runtimeState().poEntry().getValue();
    }

    /**
     * @return the translation model currently configured for translations
     */
    public Optional<AIModelDescriptor> translationAIModelDescriptor() {
        return currentAIModelDescriptor(StringPersistentProperty.AI_MODEL);
    }

    /**
     * @return the translation model currently configured for validation
     */
    public Optional<AIModelDescriptor> validationAIModelDescriptor() {
        return currentAIModelDescriptor(StringPersistentProperty.VALIDATION_AI_MODEL);
    }

    private Optional<AIModelDescriptor> currentAIModelDescriptor(StringPersistentProperty property) {
        var defaultModel = persistentSettings().get(property).filter(StringUtils::isNotBlank).orElse(null);
        if (nonNull(defaultModel)) {
            return runtimeState().aiModels().stream().filter(aiModelDescriptor -> defaultModel.equals(aiModelDescriptor.id())).findFirst();
        }
        return runtimeState().aiModels().stream().filter(AIModelDescriptor::isUsable).findFirst();
    }

    /**
     * Register the given scene to application context to listen to theme changes and other events
     *
     * @param scene
     */
    public void registerScene(Scene scene) {
        this.runtimeState().theme().subscribe(t -> {
            if (nonNull(t)) {
                Platform.runLater(() -> {
                    Application.setUserAgentStylesheet(t.getUserAgentStylesheet());
                    scene.getStylesheets().setAll(t.stylesheets());
                    if (!Platform.isSupported(ConditionalFeature.TRANSPARENT_WINDOW) && StringUtils.isNotBlank(t.transparentIncapableStylesheets())) {
                        scene.getStylesheets().addAll(t.transparentIncapableStylesheets());
                    }
                });
            }
        });
        this.persistentSettings().settingsChanges(FONT_SIZE).subscribe(size -> {
            size.filter(StringUtils::isNotBlank)
                .map(s -> String.format("-fx-font-size: %s;", s))
                .ifPresentOrElse(scene.getRoot()::setStyle, () -> scene.getRoot().setStyle(""));
        });

        this.persistentSettings()
            .get(FONT_SIZE)
            .filter(not(String::isBlank))
            .ifPresent(size -> scene.getRoot().setStyle(String.format("-fx-font-size: %s;", size)));
    }

    /**
     * Sets the injector
     */
    public void injector(Injector injector) {
        this.injector = Optional.ofNullable(injector);
    }

    /**
     * @return an instance of type
     */
    public <T> T instance(Class<T> type) {
        return injector.orElseThrow(() -> new IllegalStateException("Injector not set for this application")).instance(type);
    }

    public <T> T instance(Key<T> key) {
        return injector.orElseThrow(() -> new IllegalStateException("Injector not set for this application")).instance(key);
    }

    public void clean() {
        persistentSettings.clean();
    }

    @Override
    public void close() {
        injector.ifPresent(Injector::close);
    }
}
