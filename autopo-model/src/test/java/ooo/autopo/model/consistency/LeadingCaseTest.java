package ooo.autopo.model.consistency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
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
class LeadingCaseTest {
    private LeadingCase validator;

    @BeforeEach
    void setUp() {
        validator = new LeadingCase();
    }

    @Test
    void validUpper() {
        var original = "Hello";
        var translated = "Hola";
        String result = validator.validate(original, translated, "es");
        assertEquals(ConsistencyValidator.VALID, result);
    }

    @Test
    void validLower() {
        var original = "hello";
        var translated = "hola";
        String result = validator.validate(original, translated, "es");
        assertEquals(ConsistencyValidator.VALID, result);
    }

    @Test
    void notValid() {
        var original = "Hello";
        var translated = "hola";
        String result = validator.validate(original, translated, "es");
        assertNotEquals(ConsistencyValidator.VALID, result);
    }

    @Test
    void notValidTranslated() {
        var original = "hello";
        var translated = "Hola";
        String result = validator.validate(original, translated, "es");
        assertNotEquals(ConsistencyValidator.VALID, result);
    }

    @Test
    void excludeLanguages() {
        assertThat(validator.excludeLanguages()).containsExactlyInAnyOrder(Locale.JAPANESE.getLanguage(),
                                                                           Locale.CHINESE.getLanguage(),
                                                                           "ar",
                                                                           "th",
                                                                           "lo",
                                                                           "my",
                                                                           "bo",
                                                                           "km",
                                                                           "dz",
                                                                           "ko",
                                                                           "ka",
                                                                           "de",
                                                                           "es",
                                                                           "gl",
                                                                           "he",
                                                                           "ckb",
                                                                           "kmr",
                                                                           "hi");
    }
}