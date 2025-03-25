package ooo.autopo.app;

/*
 * This file is part of the Autopo project
 * Created 25/02/25
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
