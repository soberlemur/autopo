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
class QuestionMarkTest {

    private QuestionMark validator;

    @BeforeEach
    void setUp() {
        validator = new QuestionMark();
    }

    @Test
    void consistentQuestionMark() {
        var original = "Are you okay?";
        var translated = "¿Estás bien?";
        var result = validator.validate(original, translated, "es");
        assertEquals(ConsistencyValidator.VALID, result);
    }

    @Test
    void inconsistentQuestionMark() {
        var original = "Are you okay?";
        var translated = "Estás bien";
        var result = validator.validate(original, translated, "es");
        assertNotEquals(ConsistencyValidator.VALID, result);
    }

    @Test
    void noQuestionMarkInBoth() {
        var original = "Hello there";
        var translated = "Hola allí";
        var result = validator.validate(original, translated, "es");
        assertEquals(ConsistencyValidator.VALID, result);
    }

    @Test
    void specificQuestionMarkEquivalence() {
        var original = "How are you?";
        var translated = "Πώς είσαι;";
        var result = validator.validate(original, translated, "el");
        assertEquals(ConsistencyValidator.VALID, result);
    }

    @Test
    void inconsistentSpecificQuestionMarkEquivalence() {
        var original = "How are you";
        var translated = "Πώς είσαι;";
        var result = validator.validate(original, translated, "el");
        assertNotEquals(ConsistencyValidator.VALID, result);
    }

    @Test
    void excludeLanguages() {
        assertThat(validator.excludeLanguages()).containsExactlyInAnyOrder("ar", "fa", "ur", "syr", "hy", "bo", "dz", "es", "gl");
    }

}