package ooo.autopo.theme;
/*
 * This file is part of the Autopo project
 * Created 17/02/25
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

import java.util.List;

/**
 * @author Andrea Vacondio
 */
public interface Theme {
    /**
     * @return a unique identifier for this theme
     */
    String id();

    /**
     * @return a non null List of stylesheets paths
     */
    List<String> stylesheets();

    /**
     * @return a collection of stylesheets paths to be loaded if the platform is transparent incapable
     */
    String transparentIncapableStylesheets();

    /**
     * @return The theme name
     */
    String name();

    /**
     * @return if the theme is a dark theme
     */
    boolean isDark();

    /**
     * @return true if it can be picked up as a default theme
     */
    boolean isDefault();

    /**
     * @return the path to the theme user-agent stylesheet. See Application. setUserAgentStylesheet(String) for more info.
     */
    String getUserAgentStylesheet();
}
