package ooo.autopo.model.po;

import com.soberlemur.potentilla.Message;
import ooo.autopo.model.ai.TranslationAssessment;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
class PoEntryTest {
    @Test
    void testNullMessageThrowsException() {
        var exception = assertThrows(IllegalArgumentException.class, () -> new PoEntry(null));
        assertEquals("Message cannot be null", exception.getMessage());
    }

    @Test
    void msgstrIsUpdatedWhenTranslatedValueIsUpdated() {
        var message = new Message();
        message.setMsgId("id");
        message.setMsgstr("Test Message");
        var entry = new PoEntry(message);
        assertEquals(message.getMsgstr(), entry.translatedValue().getValue());
        entry.translatedValue().setValue("New Value");
        assertEquals("New Value", message.getMsgstr());
    }

    @Test
    void testContainsReturnsTrueForMatchInUntranslatedValue() {
        var message = new Message();
        message.setMsgId("untranslated value");
        assertTrue(new PoEntry(message).contains("untranslated"));
    }

    @Test
    void testNotifyLocaleChangeClearsWarningsAndSubscribes() {
        var message = new Message();
        message.setMsgId("id");
        message.setMsgstr("translated value");
        var entry = new PoEntry(message);
        entry.addWarning("Initial warning");
        assertEquals(1, entry.warnings().size());

        entry.notifyLocaleChange(Locale.ENGLISH);
        assertEquals(0, entry.warnings().size());

        entry.translatedValue().set("updated value");
        assertEquals(0, entry.warnings().size());
    }

    @Test
    void testNotifyLocaleChangeDoesNothingForNullLocale() {
        var message = new Message();
        message.setMsgId("id");
        message.setMsgstr("translated value");
        var entry = new PoEntry(message);

        entry.addWarning("Initial warning");
        entry.notifyLocaleChange(null);

        assertEquals(1, entry.warnings().size());
    }

    @Test
    void testWarningsClearedAndValidatorsInvokedOnTranslatedValueChange() {
        var message = new Message();
        message.setMsgId("Initial value");
        message.setMsgstr("initial value");
        var entry = new PoEntry(message);
        entry.notifyLocaleChange(Locale.ENGLISH);
        assertEquals(1, entry.warnings().size());

        entry.translatedValue().set("New translated value");
        assertEquals(0, entry.warnings().size());
    }

    @Test
    void testContainsReturnsTrueForMatchInTranslatedValue() {
        var message = new Message();
        message.setMsgstr("translated value");
        assertTrue(new PoEntry(message).contains("translated"));
    }

    @Test
    void testContainsReturnsFalseWhenNoMatch() {
        var message = new Message();
        message.setMsgId("unrelated");
        message.setMsgstr("different");
        assertFalse(new PoEntry(message).contains("missing"));
    }

    @Test
    void testContainsReturnsFalseForNullOrBlank() {
        var message = new Message();
        message.setMsgId("content");
        var entry = new PoEntry(message);
        assertFalse(entry.contains(null));
        assertFalse(entry.contains(""));
        assertFalse(entry.contains(" "));
    }

    @Test
    void testAcceptSuggestionUpdatesTranslatedValue() {
        var message = new Message();
        message.setMsgstr("original value");
        var entry = new PoEntry(message);
        entry.assessment().set(new TranslationAssessment(1, "feedback", "suggested value"));
        entry.acceptSuggestion();
        assertEquals("suggested value", entry.translatedValue().getValue());
    }

    @Test
    void testAcceptSuggestionDoesNothingIfAssessmentIsNull() {
        var message = new Message();
        message.setMsgstr("original value");
        var entry = new PoEntry(message);
        entry.acceptSuggestion();
        assertEquals("original value", entry.translatedValue().getValue());
    }

    @Test
    void testAcceptSuggestionDoesNothingIfNoSuggestedReplacement() {
        var message = new Message();
        message.setMsgstr("original value");
        var entry = new PoEntry(message);
        entry.assessment().set(new TranslationAssessment(1, "feedback", null));
        entry.acceptSuggestion();
        assertEquals("original value", entry.translatedValue().getValue());
    }
}