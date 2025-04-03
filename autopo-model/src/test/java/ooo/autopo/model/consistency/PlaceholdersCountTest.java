package ooo.autopo.model.consistency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/*
 * This file is part of the Autopo project
 * Created 03/04/25
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
class PlaceholdersCountTest {
    private PlaceholdersCount validator;

    @BeforeEach
    void setUp() {
        validator = new PlaceholdersCount();
    }

    @Test
    void consistentPlaceholders() {
        var original = "Hello %s, you have %d messages.";
        var translated = "Hola %s, tienes %d mensajes.";
        String result = validator.validate(original, translated, "es");
        assertEquals(ConsistencyValidator.VALID, result);
    }

    @Test
    void inconsistentPlaceholders() {
        var original = "Hello %s, you have %d messages.";
        var translated = "Hola %s, tienes mensajes.";
        String result = validator.validate(original, translated, "es");
        assertNotEquals(ConsistencyValidator.VALID, result);
    }

    @Test
    void noPlaceholdersInBoth() {
        var original = "Hello there.";
        var translated = "Hola allí.";
        String result = validator.validate(original, translated, "es");
        assertEquals(ConsistencyValidator.VALID, result);
    }

    @Test
    void onlyOriginalHasPlaceholders() {
        var original = "Hello %s.";
        var translated = "Hola.";
        String result = validator.validate(original, translated, "es");
        assertNotEquals(ConsistencyValidator.VALID, result);
    }

    @Test
    void onlyTranslatedHasPlaceholders() {
        var original = "Hello.";
        var translated = "Hola %s.";
        String result = validator.validate(original, translated, "es");
        assertNotEquals(ConsistencyValidator.VALID, result);
    }

    @Test
    void consistentBracedPlaceholders() {
        var original = "Hello {0}, you have {1} messages.";
        var translated = "Hola {0}, tienes {1} mensajes.";
        String result = validator.validate(original, translated, "es");
        assertEquals(ConsistencyValidator.VALID, result);
    }

    @Test
    void inconsistentBracedPlaceholders() {
        var original = "Hello {0}, you have {1} messages.";
        var translated = "Hola {0}, tienes mensajes.";
        String result = validator.validate(original, translated, "es");
        assertNotEquals(ConsistencyValidator.VALID, result);
    }
}