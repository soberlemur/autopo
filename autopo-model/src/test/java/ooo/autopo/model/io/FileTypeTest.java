package ooo.autopo.model.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * This file is part of the Autopo project
 * Created 31/03/25
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