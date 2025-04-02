package ooo.autopo.app;

/*
 * This file is part of the Autopo project
 * Created 22/02/25
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
