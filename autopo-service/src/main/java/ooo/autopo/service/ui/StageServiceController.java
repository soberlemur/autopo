package ooo.autopo.service.ui;
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

import jakarta.inject.Inject;
import ooo.autopo.model.lifecycle.CleanupRequest;
import ooo.autopo.model.ui.SetLatestStageStatusRequest;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.injector.Auto;
import org.tinylog.Logger;

import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * Controller for the {@link StageService}
 *
 * @author Andrea Vacondio
 */
@Auto
public class StageServiceController {

    private StageService service;

    @Inject
    public StageServiceController(StageService service) {
        this.service = service;
        eventStudio().addAnnotatedListeners(this);
    }

    @EventListener
    public void requestStageStatus(SetLatestStageStatusRequest event) {
        Logger.debug("Setting latest stage status to: {}", event.status());
        service.save(event.status());
    }

    @EventListener
    public void clean(CleanupRequest event) {
        Logger.debug("Cleaning up latest stage status");
        service.clean();
    }

}
