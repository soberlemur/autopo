package ooo.autopo.service.ui;
/*
 * This file is part of the Autopo project
 * Created 13/02/25
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
