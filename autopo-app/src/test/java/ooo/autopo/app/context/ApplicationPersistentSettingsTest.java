package ooo.autopo.app.context;

import ooo.autopo.app.ConfigurableSystemProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetSystemProperty;
import org.pdfsam.persistence.PersistenceException;
import org.pdfsam.persistence.PreferencesRepository;
import org.pdfsam.test.ValuesRecorder;

import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
 * This file is part of the Autopo project
 * Created 25/02/25
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
class ApplicationPersistentSettingsTest {
    private ApplicationPersistentSettings victim;
    private PreferencesRepository repo;

    @BeforeEach
    public void setUp() {
        repo = mock(PreferencesRepository.class);
        victim = new ApplicationPersistentSettings(repo);
    }

    @Test
    @DisplayName("String value is set and notified")
    public void setString() throws PersistenceException {
        var values = new ValuesRecorder<Optional<String>>();
        victim.settingsChanges(StringPersistentProperty.LOCALE).subscribe(values);
        victim.set(StringPersistentProperty.LOCALE, "it");
        verify(repo).saveString(StringPersistentProperty.LOCALE.key(), "it");
        assertThat(values.values()).containsExactly(empty(), of("it"));
    }

    @Test
    @DisplayName("Null string value is set and notified")
    public void setNullString() throws PersistenceException {
        var values = new ValuesRecorder<Optional<String>>();
        victim.settingsChanges(StringPersistentProperty.LOCALE).subscribe(values);
        victim.set(StringPersistentProperty.LOCALE, null);
        verify(repo).saveString(StringPersistentProperty.LOCALE.key(), null);
        assertThat(values.values()).containsExactly(empty());
    }

    @Test
    @DisplayName("Blank string value is set and notified")
    public void setBlankString() throws PersistenceException {
        var values = new ValuesRecorder<Optional<String>>();
        victim.settingsChanges(StringPersistentProperty.LOCALE).subscribe(values);
        victim.set(StringPersistentProperty.LOCALE, "   ");
        verify(repo).saveString(StringPersistentProperty.LOCALE.key(), "   ");
        assertThat(values.values()).containsExactly(empty(), of("   "));
    }

    @Test
    @DisplayName("Failing repo string value is not notified")
    public void negativeSetString() throws PersistenceException {
        doThrow(PersistenceException.class).when(repo).saveString(StringPersistentProperty.LOCALE.key(), "it");
        var values = new ValuesRecorder<Optional<String>>();
        victim.settingsChanges(StringPersistentProperty.LOCALE).subscribe(values);
        victim.set(StringPersistentProperty.LOCALE, "it");
        verify(repo).saveString(StringPersistentProperty.LOCALE.key(), "it");
        assertThat(values.values()).containsExactly(empty());
    }

    @Test
    @DisplayName("String value null prop")
    public void nullSetString() {
        assertThrows(IllegalArgumentException.class, () -> victim.set(null, "it"));
    }

    @Test
    @DisplayName("Integer value is set and notified")
    public void setInteger() throws PersistenceException {
        var values = new ValuesRecorder<Optional<Integer>>();
        victim.settingsChanges(IntegerPersistentProperty.LOGVIEW_ROWS_NUMBER).subscribe(values);
        victim.set(IntegerPersistentProperty.LOGVIEW_ROWS_NUMBER, 14);
        verify(repo).saveInt(IntegerPersistentProperty.LOGVIEW_ROWS_NUMBER.key(), 14);
        assertThat(values.values()).containsExactly(empty(), of(14));
    }

    @Test
    @DisplayName("Failing repo integer value is not notified")
    public void negativeSetInteger() throws PersistenceException {
        doThrow(PersistenceException.class).when(repo).saveInt(IntegerPersistentProperty.LOGVIEW_ROWS_NUMBER.key(), 14);
        var values = new ValuesRecorder<Optional<Integer>>();
        victim.settingsChanges(IntegerPersistentProperty.LOGVIEW_ROWS_NUMBER).subscribe(values);
        victim.set(IntegerPersistentProperty.LOGVIEW_ROWS_NUMBER, 14);
        verify(repo).saveInt(IntegerPersistentProperty.LOGVIEW_ROWS_NUMBER.key(), 14);
        assertThat(values.values()).containsExactly(empty());
    }

    @Test
    @DisplayName("Integer value null prop")
    public void nullSetInteger() {
        assertThrows(IllegalArgumentException.class, () -> victim.set(null, 14));
    }

    @Test
    @DisplayName("Boolean value is set and notified")
    public void setBoolean() throws PersistenceException {
        var values = new ValuesRecorder<Optional<Boolean>>();
        victim.settingsChanges(BooleanPersistentProperty.CHECK_UPDATES).subscribe(values);
        victim.set(BooleanPersistentProperty.CHECK_UPDATES, true);
        verify(repo).saveBoolean(BooleanPersistentProperty.CHECK_UPDATES.key(), true);
        assertThat(values.values()).containsExactly(empty(), of(true));
    }

    @Test
    @DisplayName("Failing repo boolean value is not notified")
    public void negativeSetBoolean() throws PersistenceException {
        doThrow(PersistenceException.class).when(repo)
                                           .saveBoolean(BooleanPersistentProperty.CHECK_UPDATES.key(), true);
        var values = new ValuesRecorder<Optional<Boolean>>();
        victim.settingsChanges(BooleanPersistentProperty.CHECK_UPDATES).subscribe(values);
        victim.set(BooleanPersistentProperty.CHECK_UPDATES, true);
        verify(repo).saveBoolean(BooleanPersistentProperty.CHECK_UPDATES.key(), true);
        assertThat(values.values()).containsExactly(empty());
    }

    @Test
    @DisplayName("Boolean value null prop")
    public void nullSetBoolean() {
        assertThrows(IllegalArgumentException.class, () -> victim.set(null, true));
    }

    @Test
    @DisplayName("Get string value")
    public void getString() throws PersistenceException {
        when(repo.getString(anyString(), any(Supplier.class))).thenReturn("it");
        assertEquals("it", victim.get(StringPersistentProperty.LOCALE).get());
    }

    @Test
    @DisplayName("Failing repo get string value")
    @SetSystemProperty(key = ConfigurableSystemProperty.LOCALE_PROP, value = "es")
    public void negativeGetString() throws PersistenceException {
        doThrow(PersistenceException.class).when(repo).getString(anyString(), any(Supplier.class));
        assertEquals("es", victim.get(StringPersistentProperty.LOCALE).get());
    }

    @Test
    @DisplayName("Get string returns no value")
    @SetSystemProperty(key = ConfigurableSystemProperty.LOCALE_PROP, value = "es")
    public void emptyGetString() throws PersistenceException {
        var repo = new PreferencesRepository("/test/org/pdfsam/delete");
        var victim = new ApplicationPersistentSettings(repo);
        assertEquals("es", victim.get(StringPersistentProperty.LOCALE).get());
        repo.clean();
    }

    @Test
    public void nullGetString() {
        assertThrows(IllegalArgumentException.class, () -> victim.get((StringPersistentProperty) null));
    }

    @Test
    @DisplayName("Get integer value")
    public void getInteger() throws PersistenceException {
        when(repo.getInt(anyString(), any(Supplier.class))).thenReturn(14);
        assertEquals(14, victim.get(IntegerPersistentProperty.LOGVIEW_ROWS_NUMBER));
    }

    @Test
    @DisplayName("Failing repo get integer value")
    public void negativeGetInteger() throws PersistenceException {
        doThrow(PersistenceException.class).when(repo).getInt(anyString(), any(Supplier.class));
        assertEquals(200, victim.get(IntegerPersistentProperty.LOGVIEW_ROWS_NUMBER));
    }

    @Test
    public void nullGetInteger() {
        assertThrows(IllegalArgumentException.class, () -> victim.get((IntegerPersistentProperty) null));
    }

    @Test
    @DisplayName("Get boolean value")
    public void getBoolean() throws PersistenceException {
        when(repo.getBoolean(anyString(), any(Supplier.class))).thenReturn(false);
        assertFalse(victim.get(BooleanPersistentProperty.CHECK_UPDATES));
    }

    @Test
    @DisplayName("Failing repo get boolean value")
    public void negativeGetBoolean() throws PersistenceException {
        when(repo.getBoolean(anyString(), any(Supplier.class))).thenThrow(PersistenceException.class);
        assertTrue(victim.get(BooleanPersistentProperty.CHECK_UPDATES));
    }

    @Test
    public void nullGetBoolean() {
        assertThrows(IllegalArgumentException.class, () -> victim.get((BooleanPersistentProperty) null));
    }

    @Test
    public void clean() throws PersistenceException {
        victim.clean();
        verify(repo).clean();
    }

    @Test
    void hasValueFor() {
        when(repo.keys()).thenReturn(new String[] {});
        assertFalse(victim.hasValueFor(StringPersistentProperty.LOCALE));
        when(repo.keys()).thenReturn(new String[] { "theme" });
        assertFalse(victim.hasValueFor(StringPersistentProperty.LOCALE));
        when(repo.keys()).thenReturn(new String[] { "theme", "locale" });
        assertTrue(victim.hasValueFor(StringPersistentProperty.LOCALE));
        when(repo.keys()).thenReturn(new String[] { "locale" });
        assertTrue(victim.hasValueFor(StringPersistentProperty.LOCALE));
    }

    @Test
    void hasValueForNullProperty() {
        assertFalse(victim.hasValueFor(null));
    }

    @Test
    void delete() {
        victim.delete(StringPersistentProperty.LOCALE);
        verify(repo).delete(StringPersistentProperty.LOCALE.key());
    }

    @Test
    void deleteNull() {
        victim.delete(null);
        verify(repo, never()).delete(StringPersistentProperty.LOCALE.key());
    }
}