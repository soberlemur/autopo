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

import com.soberlemur.potentilla.catalog.parse.ParseException;
import jakarta.inject.Inject;
import ooo.autopo.model.po.PoLoadRequest;
import ooo.autopo.model.project.ProjectLoadRequest;
import ooo.autopo.model.project.SaveProjectRequest;
import ooo.autopo.service.ServiceExceptionHandler;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.injector.Auto;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ooo.autopo.i18n.I18nContext.i18n;
import static ooo.autopo.model.LoadingStatus.LOADING;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;
import static org.sejda.commons.util.RequireUtils.requireNotNullArg;

/**
 * @author Andrea Vacondio
 */
@Auto
public class IOController {

    private final IOService ioService;
    private ServiceExceptionHandler exceptionHandler = Logger::error;
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
        executorService.submit(() -> {
            try {
                ioService.load(request.poFile());
            } catch (IOException | ParseException e) {
                exceptionHandler.accept(e, i18n().tr("An error occurred loading .po file '{0}'", request.poFile().toString()));
            }
        });
    }

    @EventListener
    public void loadProject(ProjectLoadRequest request) {
        Logger.trace("Project load request received");
        requireNotNullArg(request.project(), "Cannot load a null project");
        request.project().moveStatusTo(LOADING);
        executorService.submit(() -> {
            try {
                ioService.load(request.project());
            } catch (IOException e) {
                exceptionHandler.accept(e, i18n().tr("An error occurred loading project file '{0}'", request.project().location().toAbsolutePath().toString()));
            }
        });
    }

    @EventListener
    public void saveProject(SaveProjectRequest request) {
        Logger.trace("Project save request received");
        requireNotNullArg(request.project(), "Cannot save a null project");
        executorService.submit(() -> {
            try {
                ioService.save(request.project());
            } catch (IOException e) {
                exceptionHandler.accept(e, i18n().tr("An error occurred saving project to '{0}'", request.project().location().toAbsolutePath().toString()));
            }
        });
    }
}
