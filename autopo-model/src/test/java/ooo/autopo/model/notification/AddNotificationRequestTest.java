package ooo.autopo.model.notification;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/*
 * This file is part of the Autopo project
 * Created 31/03/25
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
class AddNotificationRequestTest {
    
    @Test
    void testValidAddNotificationRequest() {
        AddNotificationRequest request = new AddNotificationRequest(NotificationType.ERROR, "This is a message");
        assertNotNull(request);
        assertEquals(NotificationType.ERROR, request.type());
        assertEquals("This is a message", request.message());
    }

    @Test
    void testNullTypeThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new AddNotificationRequest(null, "This is a message");
        });
        assertEquals("Notification type cannot be null", exception.getMessage());
    }

    @Test
    void testBlankMessageThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new AddNotificationRequest(NotificationType.INFO, "");
        });
        assertEquals("Notification message cannot be blank", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            new AddNotificationRequest(NotificationType.INFO, "   ");
        });
        assertEquals("Notification message cannot be blank", exception.getMessage());
    }
}