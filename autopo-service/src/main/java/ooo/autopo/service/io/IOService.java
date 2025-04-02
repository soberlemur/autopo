package ooo.autopo.service.io;

/*
 * This file is part of the Autopo project
 * Created 05/02/25
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

import com.soberlemur.potentilla.catalog.parse.ParseException;
import ooo.autopo.model.ai.AIModelDescriptor;
import ooo.autopo.model.po.PoFile;
import ooo.autopo.model.po.PotFile;
import ooo.autopo.model.project.Project;

import java.io.IOException;

/**
 * @author Andrea Vacondio
 */
public interface IOService {
    void load(Project project) throws IOException;

    void load(PotFile pot) throws IOException;

    void load(PoFile poFile, AIModelDescriptor descriptor) throws IOException, ParseException;

    void updatePoFromTemplate(PoFile poFile, PotFile potFile) throws IOException;

    void save(PoFile poFile) throws IOException;

    void save(Project project) throws IOException;
}
