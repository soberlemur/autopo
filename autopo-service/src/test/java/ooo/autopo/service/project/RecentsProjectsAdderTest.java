package ooo.autopo.service.project;

import ooo.autopo.model.io.FileType;
import ooo.autopo.model.io.IOEvent;
import ooo.autopo.model.io.IOEventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pdfsam.test.ClearEventStudioExtension;

import java.nio.file.Path;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/*
 * This file is part of the Autopo project
 * Created 07/04/25
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
@ExtendWith({ ClearEventStudioExtension.class })
class RecentsProjectsAdderTest {

    private RecentsProjectsAdder victim;
    private RecentProjectsService service;

    @BeforeEach
    public void setUp() {
        service = mock(RecentProjectsService.class);
        victim = new RecentsProjectsAdder(service);
    }

    @Test
    public void validEvent() {
        var path = Path.of("valid/path");
        var event = new IOEvent(path, IOEventType.LOADED, FileType.OOO);
        victim.onProjectLoaded(event);
        verify(service).addProject(path);
    }

    @Test
    public void invalidEventType() {
        var event = new IOEvent(Path.of("valid/path"), IOEventType.SAVED, FileType.OOO);
        victim.onProjectLoaded(event);
        verify(service, never()).addProject(any());
    }

    @Test
    public void invalidEventFileType() {
        var event = new IOEvent(Path.of("valid/path"), IOEventType.LOADED, FileType.PO);
        victim.onProjectLoaded(event);
        verify(service, never()).addProject(any());
    }
}