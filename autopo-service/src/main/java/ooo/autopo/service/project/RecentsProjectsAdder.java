package ooo.autopo.service.project;

/*
 * This file is part of the Autopo project
 * Created 24/02/25
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
import ooo.autopo.model.io.IOEvent;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.injector.Auto;

import static ooo.autopo.model.io.FileType.OOO;
import static ooo.autopo.model.io.IOEventType.LOADED;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * Component adding a project the list of the recent ones when a new project is successfully loaded
 *
 * @author Andrea Vacondio
 */
@Auto
public class RecentsProjectsAdder {
    private final RecentProjectsService recentService;

    @Inject
    public RecentsProjectsAdder(RecentProjectsService recentsService) {
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
