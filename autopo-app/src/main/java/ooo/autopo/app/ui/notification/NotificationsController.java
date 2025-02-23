package ooo.autopo.app.ui.notification;
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

import jakarta.inject.Inject;
import ooo.autopo.model.notification.AddNotificationRequest;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.injector.Auto;

import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
@Auto
public class NotificationsController {

    private final NotificationsContainer container;

    @Inject
    NotificationsController(NotificationsContainer container) {
        this.container = container;
        eventStudio().addAnnotatedListeners(this);
    }

    @EventListener
    public void onAddRequest(AddNotificationRequest event) {
        container.addNotification(event.message(), event.type());
    }
}
