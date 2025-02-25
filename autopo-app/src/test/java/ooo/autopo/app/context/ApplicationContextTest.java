package ooo.autopo.app.context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.pdfsam.injector.Injector;
import org.pdfsam.persistence.PreferencesRepository;

import java.nio.file.Path;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/*
 * This file is part of the Autopo project
 * Created 25/02/25
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
class ApplicationContextTest {
    private PreferencesRepository repo;
    private ApplicationPersistentSettings persistentSettings;

    @BeforeEach
    public void setUp() {
        repo = mock(PreferencesRepository.class);
        persistentSettings = new ApplicationPersistentSettings(repo);
    }

    @Test
    void runtimeStateIsCreated(@TempDir Path tempDir) {
        var victim = new ApplicationContext(persistentSettings, null);
        Assertions.assertNotNull(victim.runtimeState());

    }

    @Test
    void clean() {
        var persistentState = mock(ApplicationPersistentSettings.class);
        var victim = new ApplicationContext(persistentState, mock(ApplicationRuntimeState.class));
        victim.clean();
        verify(persistentState).clean();
    }

    @Test
    void closeWithInjector() {
        var persistentState = mock(ApplicationPersistentSettings.class);
        var runtimeState = mock(ApplicationRuntimeState.class);
        var injector = mock(Injector.class);
        var victim = new ApplicationContext(persistentState, runtimeState);
        victim.injector(injector);
        victim.close();
        verify(injector).close();
    }
}