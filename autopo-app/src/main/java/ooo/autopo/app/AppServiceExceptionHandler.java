package ooo.autopo.app;

/*
 * This file is part of the Autopo project
 * Created 22/02/25
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

import ooo.autopo.model.notification.AddNotificationRequest;
import ooo.autopo.model.notification.NotificationType;
import ooo.autopo.service.ServiceExceptionHandler;
import org.tinylog.Logger;

import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
public class AppServiceExceptionHandler implements ServiceExceptionHandler {

    @Override
    public void accept(Throwable throwable, String message) {
        Logger.error(throwable, message);
        eventStudio().broadcast(new AddNotificationRequest(NotificationType.ERROR, message));
    }
}
