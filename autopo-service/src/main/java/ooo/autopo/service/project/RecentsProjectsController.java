package ooo.autopo.service.project;

/*
 * This file is part of the Autopo project
 * Created 24/02/25
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
import ooo.autopo.model.io.IOEvent;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.injector.Auto;

import static ooo.autopo.model.io.FileType.OOO;
import static ooo.autopo.model.io.IOEventType.LOADED;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
@Auto
public class RecentsProjectsController {
    private final RecentsService recentService;

    @Inject
    public RecentsProjectsController(RecentsService recentsService) {
        this.recentService = recentsService;
        eventStudio().addAnnotatedListeners(this);
    }

    @EventListener(priority = Integer.MIN_VALUE)
    public void onProjectLoaded(IOEvent event) {
        if (LOADED == event.type() && OOO == event.fileType()) {
            this.recentService.addProject(event.path());
        }
    }
}
