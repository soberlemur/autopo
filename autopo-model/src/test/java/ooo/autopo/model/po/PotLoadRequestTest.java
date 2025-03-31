package ooo.autopo.model.po;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
class PotLoadRequestTest {
    @Test
    void testNullPotFileThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new PotLoadRequest(null);
        });
        assertEquals("Cannot load a null potFile", exception.getMessage());
    }
}