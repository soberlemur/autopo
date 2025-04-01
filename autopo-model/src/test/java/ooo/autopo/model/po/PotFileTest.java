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
 * You are not permitted to distribute it in any form unless explicit
 * consent is given by Sober Lemur S.r.l..
 * You are not permitted to modify it.
 *
 * Autopo is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
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