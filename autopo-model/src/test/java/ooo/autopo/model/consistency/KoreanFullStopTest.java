package ooo.autopo.model.consistency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
class KoreanFullStopTest {
    private KoreanFullStop validator;

    @BeforeEach
    void setUp() {
        validator = new KoreanFullStop();
    }

    @Test
    void consistentFullStop() {
        var original = "Hello.";
        var translated = "안녕하세요.";
        String result = validator.validate(original, translated, "ko");
        assertEquals(ConsistencyValidator.VALID, result);
    }

    @Test
    void inconsistentFullStop() {
        var original = "Hello.";
        var translated = "안녕하세요";
        String result = validator.validate(original, translated, "ko");
        assertNotEquals(ConsistencyValidator.VALID, result);
    }

    @Test
    void noFullStopInBoth() {
        var original = "Hello";
        var translated = "안녕하세요";
        String result = validator.validate(original, translated, "ko");
        assertEquals(ConsistencyValidator.VALID, result);
    }

    @Test
    void specificFullStopEquivalence() {
        var original = "Hello.";
        var translated = "안녕하세요。";
        String result = validator.validate(original, translated, "ko");
        assertEquals(ConsistencyValidator.VALID, result);
    }

    @Test
    void inconsistentSpecificFullStopEquivalence() {
        var original = "Hello";
        var translated = "안녕하세요。";
        String result = validator.validate(original, translated, "ko");
        assertNotEquals(ConsistencyValidator.VALID, result);
    }

    @Test
    void inconsistentFullStopTranslated() {
        var original = "Hello";
        var translated = "안녕하세요.";
        String result = validator.validate(original, translated, "ko");
        assertNotEquals(ConsistencyValidator.VALID, result);
    }

    @Test
    void includesLanguages() {
        assertThat(validator.includeLanguages()).containsExactlyInAnyOrder("ko");
    }
}