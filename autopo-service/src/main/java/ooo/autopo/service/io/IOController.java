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
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.injector.Auto;
import org.tinylog.Logger;

import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
@Auto
public class IOController {

    private final IOService ioService;

    @Inject
    public IOController(IOService ioService) {
        this.ioService = ioService;
        eventStudio().addAnnotatedListeners(this);
    }

    @EventListener
    public void request(PoLoadRequest request) {
        Logger.trace("PO load request received");
        Thread.ofVirtual().name("po-loading-thread").start(() -> ioService.load(request.poFile()));
    }
}
