package ooo.autopo.app.context;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.pdfsam.test.ValuesRecorder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * This file is part of the Autopo project
 * Created 25/02/25
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
@Execution(ExecutionMode.SAME_THREAD)
public class ApplicationRuntimeStateTest {

    private ApplicationRuntimeState victim;

    @BeforeEach
    public void setUp() {
        victim = new ApplicationRuntimeState();
    }

    @Test
    @DisplayName("Existing working Path")
    public void positiveWorkingPath(@TempDir Path tempDir) {
        var values = new ValuesRecorder<Path>();
        victim.workingPath().subscribe(values);
        victim.workingPath(tempDir);
        assertThat(values.values()).containsExactly(null, tempDir);
    }

    @Test
    @DisplayName("Existing working path as String")
    public void positiveWorkingPathString(@TempDir Path tempDir) {
        var values = new ValuesRecorder<Path>();
        victim.workingPath().subscribe(values);
        victim.workingPath(tempDir.toString());
        assertThat(values.values()).containsExactly(null, tempDir);
    }

    @Test
    @DisplayName("Null working Path")
    public void nullWorkingPath(@TempDir Path tempDir) {
        var values = new ValuesRecorder<Path>();
        victim.workingPath().subscribe(values);
        victim.workingPath(tempDir);
        victim.workingPath((Path) null);
        assertThat(values.values()).containsExactly(null, tempDir, null);
    }

    @Test
    @DisplayName("Null working path as String")
    public void nullWorkingPathString(@TempDir Path tempDir) {
        var values = new ValuesRecorder<Path>();
        victim.workingPath().subscribe(values);
        victim.workingPath(tempDir);
        victim.workingPath((String) null);
        assertThat(values.values()).containsExactly(null, tempDir, null);
    }

    @Test
    @DisplayName("Blank working path as String")
    public void blankWorkingPathString(@TempDir Path tempDir) {
        var values = new ValuesRecorder<Path>();
        victim.workingPath().subscribe(values);
        victim.workingPath(tempDir);
        victim.workingPath("  ");
        assertThat(values.values()).containsExactly(null, tempDir, null);
    }

    @Test
    @DisplayName("File working Path")
    public void fileWorkingPath(@TempDir Path tempDir) throws IOException {
        var values = new ValuesRecorder<Path>();
        victim.workingPath().subscribe(values);
        victim.workingPath(tempDir);
        var another = Files.createTempFile("test", ".tmp");
        victim.workingPath(another);
        assertThat(values.values()).containsExactly(null, tempDir, another.getParent());
    }

}