package ooo.autopo.app.context;

/*
 * This file is part of the Autopo project
 * Created 13/02/25
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

import javafx.scene.Scene;
import org.apache.commons.lang3.StringUtils;
import org.pdfsam.injector.Injector;
import org.pdfsam.injector.Key;
import org.pdfsam.persistence.PreferencesRepository;

import java.io.Closeable;
import java.util.Objects;
import java.util.Optional;

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
     * Register the given scene to application context to listen to theme changes and other events
     *
     * @param scene
     */
    public void registerScene(Scene scene) {
        this.persistentSettings().settingsChanges(FONT_SIZE).subscribe(size -> {
            size.filter(StringUtils::isNotBlank).map(s -> String.format("-fx-font-size: %s;", s))
                .ifPresentOrElse(scene.getRoot()::setStyle, () -> scene.getRoot().setStyle(""));
        });

        this.persistentSettings().get(FONT_SIZE).filter(not(String::isBlank))
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
        return injector.orElseThrow(() -> new IllegalStateException("Injector not set for this application"))
                       .instance(type);
    }

    public <T> T instance(Key<T> key) {
        return injector.orElseThrow(() -> new IllegalStateException("Injector not set for this application"))
                       .instance(key);
    }

    public void clean() {
        persistentSettings.clean();
    }

    @Override
    public void close() {
        injector.ifPresent(Injector::close);
    }
}
