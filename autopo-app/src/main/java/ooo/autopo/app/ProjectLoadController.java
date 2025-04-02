package ooo.autopo.app;

/*
 * This file is part of the Autopo project
 * Created 25/02/25
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

import javafx.util.Subscription;
import ooo.autopo.model.LoadingStatus;
import ooo.autopo.model.po.PoLoadRequest;
import ooo.autopo.model.po.PotLoadRequest;
import ooo.autopo.model.project.ProjectLoadRequest;
import org.pdfsam.injector.Auto;

import java.util.Optional;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ooo.autopo.app.context.ApplicationContext.app;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
@Auto
public class ProjectLoadController {

    public ProjectLoadController() {
        app().runtimeState().project().subscribe(p -> Optional.ofNullable(p).map(ProjectLoadRequest::new).ifPresent(r -> {
            eventStudio().broadcast(r);
            final Subscription[] subscription = new Subscription[1];
            subscription[0] = r.project().status().subscribe(status -> {
                if (status == LoadingStatus.LOADED) {
                    r.project()
                     .translations()
                     .stream()
                     .map(e -> new PoLoadRequest(e, app().translationAIModelDescriptor().orElse(null), true))
                     .forEach(eventStudio()::broadcast);
                    r.project().pot().subscribe(pot -> {
                        if (nonNull(pot)) {
                            eventStudio().broadcast(new PotLoadRequest(p.pot().get()));
                        }
                    });
                    ofNullable(subscription[0]).ifPresent(Subscription::unsubscribe);
                }
            });
        }));

    }
}
