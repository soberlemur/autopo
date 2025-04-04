package ooo.autopo.service.io;

/*
 * This file is part of the Autopo project
 * Created 05/02/25
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

import com.soberlemur.potentilla.catalog.parse.ParseException;
import jakarta.inject.Inject;
import javafx.application.Platform;
import ooo.autopo.model.lifecycle.ShutdownEvent;
import ooo.autopo.model.po.PoAddRequest;
import ooo.autopo.model.po.PoLoadRequest;
import ooo.autopo.model.po.PoSaveRequest;
import ooo.autopo.model.po.PoUpdateRequest;
import ooo.autopo.model.po.PotLoadRequest;
import ooo.autopo.model.project.ProjectLoadRequest;
import ooo.autopo.model.project.ProjectSaveRequest;
import ooo.autopo.model.ui.TranslationsCountChanged;
import ooo.autopo.service.ServiceExceptionHandler;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.injector.Auto;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Optional.ofNullable;
import static ooo.autopo.i18n.I18nContext.i18n;
import static ooo.autopo.model.LoadingStatus.LOADED;
import static ooo.autopo.model.LoadingStatus.LOADING;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
@Auto
public class IOController {

    private final IOService ioService;
    private final ServiceExceptionHandler exceptionHandler;
    private final ExecutorService mainExecutor = Executors.newSingleThreadExecutor(Thread.ofPlatform().name("io-thread-", 0).factory());
    private final ExecutorService backgroundExecutor = Executors.newFixedThreadPool(5, Thread.ofPlatform().name("po-loading-thread-", 0).factory());

    @Inject
    public IOController(IOService ioService, ServiceExceptionHandler exceptionHandler) {
        this.ioService = ioService;
        this.exceptionHandler = exceptionHandler;
        eventStudio().addAnnotatedListeners(this);
    }

    @EventListener
    public void loadPo(PoLoadRequest request) {
        Logger.trace("Po load request received");
        executor(request.background()).submit(() -> {
            try {
                ioService.load(request.poFile(), request.descriptor());
            } catch (IOException | ParseException e) {
                exceptionHandler.accept(e, i18n().tr("An error occurred loading .po file '{0}'", request.poFile().toString()));
            }
        });
    }

    @EventListener
    public void loadPot(PotLoadRequest request) {
        Logger.trace("Pot load request received");
        executor(true).submit(() -> {
            try {
                ioService.load(request.potFile());
            } catch (IOException | ParseException e) {
                exceptionHandler.accept(e, i18n().tr("An error occurred loading .pot file '{0}'", request.potFile().toString()));
            }
        });
    }

    @EventListener
    public void updatePo(PoUpdateRequest request) {
        Logger.trace("Po update load request received");
        executor(true).submit(() -> {
            for (var po : ofNullable(request.poFiles()).orElse(Collections.emptyList())) {
                try {
                    ioService.updatePoFromTemplate(po, request.potFile());
                } catch (IOException e) {
                    exceptionHandler.accept(e, i18n().tr("An error occurred updating .po file '{0}'", po.toString()));
                }
            }
            Platform.runLater(() -> {
                eventStudio().broadcast(TranslationsCountChanged.INSTANCE);
                request.complete().set(true);
            });
        });
    }

    @EventListener
    public void addPo(PoAddRequest request) {
        Logger.trace("Po addition request received");
        mainExecutor.submit(() -> {
            try {
                request.poFile().status(LOADED);
                request.project().addTranslation(request.poFile());
                ioService.updatePoFromTemplate(request.poFile(), request.project().pot().get());
            } catch (IOException e) {
                exceptionHandler.accept(e, i18n().tr("An error adding .po file '{0}' to the project", request.poFile().toString()));
            }
            Platform.runLater(() -> request.complete().set(true));
        });
    }

    @EventListener
    public void savePo(PoSaveRequest request) {
        Logger.trace("Po save request received");
        Platform.runLater(() -> request.poFile().modified(false));
        mainExecutor.submit(() -> {
            try {
                ioService.save(request.poFile());
                Platform.runLater(() -> eventStudio().broadcast(TranslationsCountChanged.INSTANCE));
            } catch (IOException e) {
                Platform.runLater(() -> request.poFile().modified(true));
                exceptionHandler.accept(e, i18n().tr("An error occurred saving .po file to '{0}'", request.poFile().toString()));
            }
        });
    }

    @EventListener
    public void loadProject(ProjectLoadRequest request) {
        Logger.trace("Project load request received");
        request.project().status(LOADING);
        mainExecutor.submit(() -> {
            try {
                ioService.load(request.project());
            } catch (IOException e) {
                exceptionHandler.accept(e, i18n().tr("An error occurred loading project file '{0}'", request.project().location().toAbsolutePath().toString()));
            }
        });
    }

    @EventListener
    public void saveProject(ProjectSaveRequest request) {
        Logger.trace("Project save request received");
        mainExecutor.submit(() -> {
            try {
                ioService.save(request.project());
            } catch (IOException e) {
                exceptionHandler.accept(e, i18n().tr("An error occurred saving project to '{0}'", request.project().location().toAbsolutePath().toString()));
            }
        });
    }

    private ExecutorService executor(boolean background) {
        if (background) {
            return backgroundExecutor;
        }
        return mainExecutor;
    }

    @EventListener
    public void onShutdown(ShutdownEvent event) {
        backgroundExecutor.shutdownNow();
        mainExecutor.shutdownNow();
    }
}
