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

import javafx.application.HostServices;
import ooo.autopo.app.ApplicationTitleController;
import ooo.autopo.app.AutopoDescriptor;
import ooo.autopo.app.WindowStatusController;
import ooo.autopo.app.ui.notification.NotificationsController;
import org.pdfsam.injector.Auto;
import org.pdfsam.injector.Components;
import org.pdfsam.injector.Provides;

import java.io.IOException;

/**
 * @author Andrea Vacondio
 */
@Components({ WindowStatusController.class, ApplicationTitleController.class, NotificationsController.class })
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
    @Auto
    public AutopoDescriptor descriptor() throws IOException {
        return new AutopoDescriptor();
    }
}
