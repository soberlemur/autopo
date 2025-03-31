package ooo.autopo.model.po;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

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
 */class PoUpdateRequestTest {
    @Test
    void testNullPotFileThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new PoUpdateRequest(null, List.of(mock(PoFile.class)));
        });
        assertEquals("Cannot update Po files using a null potFile", exception.getMessage());
    }
}