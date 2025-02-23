package ooo.autopo.app;
/*
 * This file is part of the Autopo project
 * Created 13/02/25
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
import org.tinylog.Logger;

import java.lang.Thread.UncaughtExceptionHandler;

import static ooo.autopo.i18n.I18nContext.i18n;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * {@link UncaughtExceptionHandler} that simply logs the exception
 *
 * @author Andrea Vacondio
 */
public class UncaughtExceptionLogger implements UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Logger.error(e, "Unexpected error");
        eventStudio().broadcast(new AddNotificationRequest(NotificationType.ERROR, i18n().tr("Unexpected error")));
    }

}
