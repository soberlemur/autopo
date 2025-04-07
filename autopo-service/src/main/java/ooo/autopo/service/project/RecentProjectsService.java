package ooo.autopo.service.project;

/*
 * This file is part of the Autopo project
 * Created 24/02/25
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

import java.nio.file.Path;
import java.util.List;

/**
 * Service handling the recent projects
 *
 * @author Andrea Vacondio
 */
public interface RecentProjectsService {
    /**
     * Add the path to the list of recent projects
     *
     * @param projectPath
     */
    void addProject(Path projectPath);

    /**
     * @return the list of recent projects paths
     */
    List<String> getRecentProjects();

    /**
     * clear the recent projects
     */
    void clear();
}
