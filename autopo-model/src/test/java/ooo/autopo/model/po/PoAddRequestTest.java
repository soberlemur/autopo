package ooo.autopo.model.po;

import ooo.autopo.model.project.Project;
import org.junit.jupiter.api.Test;

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
 */class PoAddRequestTest {
    @Test
    void testNullProjectFileThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new PoAddRequest(null, mock(PoFile.class));
        });
        assertEquals("Cannot add a po file to a null project", exception.getMessage());
    }

    @Test
    void testNullPoFileThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new PoAddRequest(mock(Project.class), null);
        });
        assertEquals("Cannot add a null po file to a project", exception.getMessage());
    }
}