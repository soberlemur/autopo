package ooo.autopo.model.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
class FileTypeTest {

    @Test
    void testMatches() {
        assertTrue(FileType.ALL.matches("anyfile.any"));
        assertTrue(FileType.OOO.matches("file.ooo"));
        assertTrue(FileType.OOO.matches("file.OOO"));
        assertFalse(FileType.OOO.matches("file.txt"));

        assertTrue(FileType.POT.matches("file.pot"));
        assertTrue(FileType.POT.matches("file.POT"));
        assertFalse(FileType.POT.matches("file.txt"));

        assertTrue(FileType.TXT.matches("file.txt"));
        assertTrue(FileType.TXT.matches("file.TXT"));
        assertFalse(FileType.TXT.matches("file.log"));

        assertTrue(FileType.LOG.matches("file.log"));
        assertTrue(FileType.LOG.matches("file.LOG"));
        assertTrue(FileType.LOG.matches("file.txt"));
        assertTrue(FileType.LOG.matches("file.TXT"));
        assertFalse(FileType.LOG.matches("file.pot"));

        assertTrue(FileType.PO.matches("file.po"));
        assertTrue(FileType.PO.matches("file.PO"));
        assertFalse(FileType.PO.matches("file.txt"));
    }
}