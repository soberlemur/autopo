package ooo.autopo.model.po;

import com.soberlemur.potentilla.Catalog;
import com.soberlemur.potentilla.Message;
import com.soberlemur.potentilla.MessageKey;
import ooo.autopo.model.LoadingStatus;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
class PoFileTest {
    @Test
    void testNullMessageThrowsException() {
        var exception = assertThrows(IllegalArgumentException.class, () -> new PoFile(null));
        assertEquals("The input .po file cannot be null", exception.getMessage());
    }

    @Test
    void testClearObsoleteRemovesObsoleteMessages() {
        var poFile = new PoFile(Paths.get("src/test/resources/test.po"));
        poFile.status(LoadingStatus.LOADED);
        var obsoleteMessage = new Message();
        obsoleteMessage.setMsgId("obsolete");
        obsoleteMessage.markObsolete();
        var nonObsoleteMessage = new Message();
        nonObsoleteMessage.setMsgId("valid");
        var catalog = new Catalog();
        catalog.add(obsoleteMessage);
        catalog.add(nonObsoleteMessage);
        poFile.catalog(catalog);
        assertEquals(2, catalog.size());
        assertTrue(poFile.clearObsolete());
        assertEquals(1, catalog.size());
        assertEquals("valid", poFile.entries().get(0).untranslatedValue().getValue());
        assertFalse(catalog.contains(new MessageKey(obsoleteMessage)));
        assertTrue(poFile.modifiedProperty().get());
    }

    @Test
    void testClearObsoleteDoesNotModifyNonObsoleteMessages() {
        var poFile = new PoFile(Paths.get("src/test/resources/test.po"));
        poFile.status(LoadingStatus.LOADED);
        var obsoleteMessage = new Message();
        obsoleteMessage.setMsgId("obsolete");
        var nonObsoleteMessage = new Message();
        nonObsoleteMessage.setMsgId("valid");
        var catalog = new Catalog();
        catalog.add(obsoleteMessage);
        catalog.add(nonObsoleteMessage);
        poFile.catalog(catalog);
        assertEquals(2, catalog.size());
        assertFalse(poFile.clearObsolete());
        assertEquals(2, catalog.size());
        assertTrue(catalog.contains(new MessageKey(obsoleteMessage)));
        assertFalse(poFile.modifiedProperty().get());
    }

    @Test
    void testClearObsoleteOnNonLoadedFileThrowsException() {
        var exception = assertThrows(IllegalStateException.class, new PoFile(Paths.get("src/test/resources/test.po"))::clearObsolete);
        assertEquals("Cannot clear obsolete messages from a non loaded po file", exception.getMessage());
    }

    @Test
    void testPoFileReturnsCorrectPath() {
        var expectedPath = Paths.get("src/test/resources/test.po");
        assertEquals(expectedPath, new PoFile(expectedPath).poFile());
    }

    @Test
    void testLocaleReturnsNullInitially() {
        var poFile = new PoFile(Paths.get("src/test/resources/test.po"));
        assertNull(poFile.locale().get());
    }

    @Test
    void testLocaleSetterUpdatesProperty() {
        var poFile = new PoFile(Paths.get("src/test/resources/test.po"));
        poFile.locale(Locale.FRANCE);
        assertEquals(Locale.FRANCE, poFile.locale().get());
    }

    @Test
    void testLocaleSetterThrowsNullPointerException() {
        var poFile = new PoFile(Paths.get("src/test/resources/test.po"));
        assertThrows(NullPointerException.class, () -> poFile.locale(null));
    }

    @Test
    void testEntriesAreNotified() {
        var poFile = new PoFile(Paths.get("src/test/resources/test.po"));
        var entry = mock(PoEntry.class);
        poFile.addEntry(entry);
        poFile.locale(Locale.FRANCE);
        assertEquals(Locale.FRANCE, poFile.locale().get());
        verify(entry).notifyLocaleChange(Locale.FRANCE);
    }

    @Test
    void testCatalogInitiallyNull() {
        var poFile = new PoFile(Paths.get("src/test/resources/test.po"));
        assertNull(poFile.catalog(), "Catalog should be null initially");
    }

    @Test
    void testCatalogSetterThrowsNullPointerException() {
        var poFile = new PoFile(Paths.get("src/test/resources/test.po"));
        assertThrows(NullPointerException.class, () -> poFile.catalog(null));
    }

    @Test
    void testCatalogSetterUpdatesCatalogAndClearsEntries() {
        var poFile = new PoFile(Paths.get("src/test/resources/test.po"));
        var catalog = new Catalog();
        var entry = mock(PoEntry.class);

        poFile.addEntry(entry);
        poFile.catalog(catalog);

        assertEquals(catalog, poFile.catalog(), "Catalog should be updated");
        assertEquals(0, poFile.entries().size(), "Entries should be cleared when setting the catalog");
    }

    @Test
    void testCatalogSetterAddsNonObsoleteEntriesAndNotifiesLocale() {
        var poFile = new PoFile(Paths.get("src/test/resources/test.po"));
        var catalog = new Catalog();
        var message1 = new Message();
        message1.setMsgId("id");
        message1.setMsgstr("text");
        catalog.add(message1);
        poFile.catalog(catalog);
        assertFalse(message1.isObsolete());
        assertEquals(1, poFile.entries().size());
        assertEquals("text", poFile.entries().get(0).translatedValue().get());

        var catalog2 = new Catalog();
        var message2 = new Message();
        message2.setMsgId("another");
        message2.setMsgstr("another text");
        catalog2.add(message2);
        var message3 = new Message();
        message3.markObsolete();
        catalog2.add(message3);
        poFile.catalog(catalog2);

        assertEquals(1, poFile.entries().size());
        assertEquals("another text", poFile.entries().get(0).translatedValue().get());
    }

    @Test
    void testUpdatePercentageWithNoEntries() {
        var poFile = new PoFile(Paths.get("src/test/resources/test.po"));
        poFile.updatePercentageOfTranslation();
        assertEquals(0.0, poFile.translationPercentage().get());
    }

    @Test
    void allTranslated() {
        var poFile = new PoFile(Paths.get("src/test/resources/test.po"));
        var message = new Message();
        message.setMsgId("another");
        message.setMsgstr("another text");
        poFile.addEntry(new PoEntry(message));
        assertEquals(0, poFile.translationPercentage().get());
        poFile.updatePercentageOfTranslation();
        assertEquals(1, poFile.translationPercentage().get());
    }

    @Test
    void testUpdatePercentageWithPartiallyTranslatedEntries() {
        var poFile = new PoFile(Paths.get("src/test/resources/test.po"));
        var message = new Message();
        message.setMsgId("another");
        message.setMsgstr("another text");
        poFile.addEntry(new PoEntry(message));
        poFile.addEntry(new PoEntry(new Message()));
        poFile.updatePercentageOfTranslation();
        assertEquals(0.5, poFile.translationPercentage().get());
    }

    @Test
    void testSetAndGetStatus() {
        var poFile = new PoFile(Path.of("example.pot"));
        poFile.status(LoadingStatus.LOADING);
        assertEquals(LoadingStatus.LOADING, poFile.status().getValue());
    }

    @Test
    void testSetStatusThrowsExceptionForNullStatus() {
        var poFile = new PoFile(Path.of("example.pot"));
        var exception = assertThrows(NullPointerException.class, () -> poFile.status(null));
        assertNull(exception.getMessage());
    }

    @Test
    void testCompareAndSetStatusSuccess() {
        var poFile = new PoFile(Path.of("example.pot"));
        assertTrue(poFile.status(LoadingStatus.INITIAL, LoadingStatus.LOADING));
        assertEquals(LoadingStatus.LOADING, poFile.status().getValue());
    }

    @Test
    void testCompareAndSetStatusFailure() {
        var poFile = new PoFile(Path.of("example.pot"));
        poFile.status(LoadingStatus.CANCELLED);
        assertFalse(poFile.status(LoadingStatus.INITIAL, LoadingStatus.LOADING));
        assertEquals(LoadingStatus.CANCELLED, poFile.status().getValue());
    }

    @Test
    void testIsLoaded() {
        var poFile = new PoFile(Path.of("example.pot"));
        assertFalse(poFile.isLoaded());
        poFile.status(LoadingStatus.LOADED);
        assertTrue(poFile.isLoaded());
    }

    @Test
    void testUpdateFromTemplateUpdatesCatalogAndEntries() {
        var poFile = new PoFile(Path.of("poFile.po"));
        var potFile = new PotFile(Path.of("templateFile.pot"));

        // Setup template catalog
        var templateCatalog = new Catalog().asTemplate();
        var messageFromTemplate = new Message();
        messageFromTemplate.setMsgId("templateMessage");
        messageFromTemplate.setMsgstr("templateTranslation");
        templateCatalog.add(messageFromTemplate);
        potFile.catalog(templateCatalog);

        // Setup current catalog
        var currentCatalog = new Catalog();
        var existingMessage = new Message();
        existingMessage.setMsgId("existingMessage");
        existingMessage.setMsgstr("existingTranslation");
        currentCatalog.add(existingMessage);
        poFile.catalog(currentCatalog);

        // Act
        poFile.updateFromTemplate(potFile);

        // Assert
        assertEquals(1, poFile.entries().size());
        assertEquals("templateTranslation", poFile.entries().get(0).translatedValue().get());
        assertTrue(currentCatalog.get(new MessageKey(existingMessage)).isObsolete());
    }

}