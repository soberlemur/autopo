package ooo.autopo.service.io;

/*
 * This file is part of the Autopo project
 * Created 05/02/25
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
import ooo.autopo.model.PoLoadRequest;
import ooo.autopo.model.project.LoadProjectRequest;
import ooo.autopo.model.project.SaveProjectRequest;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.injector.Auto;
import org.tinylog.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ooo.autopo.model.LoadingStatus.LOADING;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;
import static org.sejda.commons.util.RequireUtils.requireNotNullArg;

/**
 * @author Andrea Vacondio
 */
@Auto
public class IOController {

    private final IOService ioService;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor(Thread.ofVirtual().name("io-thread-", 0).factory());

    @Inject
    public IOController(IOService ioService) {
        this.ioService = ioService;
        eventStudio().addAnnotatedListeners(this);
    }

    @EventListener
    public void loadPo(PoLoadRequest request) {
        Logger.trace("PO load request received");
        requireNotNullArg(request.poFile(), "Cannot load a null poFile");
        request.poFile().moveStatusTo(LOADING);
        executorService.submit(() -> ioService.load(request.poFile()));
    }

    @EventListener
    public void loadProject(LoadProjectRequest request) {
        Logger.trace("Project load request received");
        requireNotNullArg(request.project(), "Cannot load a null project");
        request.project().moveStatusTo(LOADING);
        executorService.submit(() -> ioService.load(request.project()));
    }

    @EventListener
    public void saveProject(SaveProjectRequest request) {
        Logger.trace("Project save request received");
        requireNotNullArg(request.project(), "Cannot save a null project");
        executorService.submit(() -> ioService.save(request.project()));
    }
}
