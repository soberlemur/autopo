package ooo.autopo.model.po;

import com.soberlemur.potentilla.Catalog;
import ooo.autopo.model.LoadingStatus;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * This file is part of the Autopo project
 * Created 01/04/25
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
class PotFileTest {
    @Test
    void testNullMessageThrowsException() {
        var exception = assertThrows(IllegalArgumentException.class, () -> new PotFile(null));
        assertEquals("The input .pot file cannot be null", exception.getMessage());
    }

    @Test
    void testSetAndGetCatalog() {
        var potFile = new PotFile(Path.of("example.pot"));
        var catalog = new Catalog(true);
        potFile.catalog(catalog);
        assertEquals(catalog, potFile.catalog());
    }

    @Test
    void testSetCatalogThrowsExceptionForNullCatalog() {
        var potFile = new PotFile(Path.of("example.pot"));
        var exception = assertThrows(NullPointerException.class, () -> potFile.catalog(null));
        assertNull(exception.getMessage());
    }

    @Test
    void testSetCatalogThrowsExceptionForNonTemplateCatalog() {
        var potFile = new PotFile(Path.of("example.pot"));

        var exception = assertThrows(IllegalArgumentException.class, () -> potFile.catalog(new Catalog(false)));
        assertEquals("Cannot set a non template catalog", exception.getMessage());
    }

    @Test
    void testSetAndGetStatus() {
        var potFile = new PotFile(Path.of("example.pot"));
        potFile.status(LoadingStatus.LOADING);
        assertEquals(LoadingStatus.LOADING, potFile.status().getValue());
    }

    @Test
    void testSetStatusThrowsExceptionForNullStatus() {
        var potFile = new PotFile(Path.of("example.pot"));
        var exception = assertThrows(NullPointerException.class, () -> potFile.status(null));
        assertNull(exception.getMessage());
    }

    @Test
    void testCompareAndSetStatusSuccess() {
        var potFile = new PotFile(Path.of("example.pot"));
        assertTrue(potFile.status(LoadingStatus.INITIAL, LoadingStatus.LOADING));
        assertEquals(LoadingStatus.LOADING, potFile.status().getValue());
    }

    @Test
    void testCompareAndSetStatusFailure() {
        var potFile = new PotFile(Path.of("example.pot"));
        potFile.status(LoadingStatus.CANCELLED);
        assertFalse(potFile.status(LoadingStatus.INITIAL, LoadingStatus.LOADING));
        assertEquals(LoadingStatus.CANCELLED, potFile.status().getValue());
    }

    @Test
    void testIsLoaded() {
        var potFile = new PotFile(Path.of("example.pot"));
        assertFalse(potFile.isLoaded());
        potFile.status(LoadingStatus.LOADED);
        assertTrue(potFile.isLoaded());
    }
}