package ooo.autopo.app;

import ooo.autopo.model.notification.AddNotificationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;
import org.pdfsam.eventstudio.Listener;
import org.pdfsam.test.ClearEventStudioExtension;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/*
 * This file is part of the Autopo project
 * Created 07/04/25
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
@ExtendWith({ ClearEventStudioExtension.class })
class AppServiceExceptionHandlerTest {

    @RegisterExtension
    static ClearEventStudioExtension staticExtension = new ClearEventStudioExtension();

    @Test
    public void requestIsSent() {
        var victim = new AppServiceExceptionHandler();
        var listener = Mockito.mock(Listener.class);
        eventStudio().add(AddNotificationRequest.class, listener);
        victim.accept(new IOException(), "Message");
        verify(listener).onEvent(Mockito.any(AddNotificationRequest.class));
    }
}