package ooo.autopo.app.config;
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

import jakarta.inject.Named;
import javafx.application.HostServices;
import ooo.autopo.app.AppServiceExceptionHandler;
import ooo.autopo.app.ApplicationTitleController;
import ooo.autopo.app.AutopoDescriptor;
import ooo.autopo.app.ModalPaneController;
import ooo.autopo.app.PoLoadController;
import ooo.autopo.app.ProjectLoadController;
import ooo.autopo.app.WindowStatusController;
import ooo.autopo.app.ui.AppContentController;
import ooo.autopo.app.ui.OverlayItem;
import ooo.autopo.app.ui.about.AboutOverlayItem;
import ooo.autopo.app.ui.logs.LogsOverlayItem;
import ooo.autopo.app.ui.notification.NotificationsController;
import ooo.autopo.app.ui.project.ProjectSettingsOverlay;
import ooo.autopo.app.ui.settings.SettingsOverlayItem;
import ooo.autopo.model.AppDescriptor;
import ooo.autopo.service.ServiceExceptionHandler;
import org.pdfsam.injector.Components;
import org.pdfsam.injector.Provides;

import java.io.IOException;
import java.util.List;

/**
 * @author Andrea Vacondio
 */
@Components({ WindowStatusController.class, ApplicationTitleController.class, NotificationsController.class, AppContentController.class,
        ProjectLoadController.class, PoLoadController.class, ModalPaneController.class })
public class AppConfig {

    private final HostServices services;

    public AppConfig(HostServices services) {
        this.services = services;
    }

    @Provides
    public HostServices hostServices() {
        return services;
    }

    @Provides
    public AppDescriptor descriptor() throws IOException {
        return new AutopoDescriptor();
    }

    @Provides
    @Named("overlays")
    public List<OverlayItem> overlays() {
        return List.of(new AboutOverlayItem(), new SettingsOverlayItem(), new LogsOverlayItem(), new ProjectSettingsOverlay());
    }

    @Provides
    public ServiceExceptionHandler exceptionHandler() {
        return new AppServiceExceptionHandler();
    }
}
