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

import ooo.autopo.model.LoadingStatus;
import ooo.autopo.model.po.PoLoadRequest;
import org.pdfsam.injector.Auto;

import static java.util.Optional.ofNullable;
import static ooo.autopo.app.context.ApplicationContext.app;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
@Auto
public class PoLoadController {

    public PoLoadController() {
        app().runtimeState()
             .poFile()
             .subscribe(p -> ofNullable(p)
                     .filter(f -> f.status().getValue() == LoadingStatus.INITIAL)
                     .map(PoLoadRequest::new)
                     .ifPresent(eventStudio()::broadcast));
    }
}
