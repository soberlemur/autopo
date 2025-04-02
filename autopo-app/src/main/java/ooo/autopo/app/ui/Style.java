package ooo.autopo.app.ui;
/*
 * This file is part of the Autopo project
 * Created 13/02/25
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

/**
 * @author Andrea Vacondio
 */
public enum Style {
    CONTAINER("autopo-container"),
    BUTTON("btn"),
    SAVE_BUTTON("btn", "toolbar-btn", "save-btn"),
    CLOSE_BUTTON("btn", "close-btn"),
    TOOLBAR_BUTTON("btn", "toolbar-btn"),
    TOOLBAR_BUTTON_RIGHT("btn", "btn-right", "toolbar-btn"),
    TOOLBAR_BUTTON_LEFT("btn", "btn-left", "toolbar-btn"),
    TOOLBAR_BUTTON_CENTER("btn", "btn-center", "toolbar-btn"),
    OVERLAY_BUTTON("btn", "page-tile-overlay-btn"),
    SIDEBAR_BUTTON("btn", "sidebar-button"),
    GRID("grid"),
    TOOLBAR("tool-toolbar-buttons");

    public static final int DEFAULT_SPACING = 5;
    private final String[] classes;

    Style(String... classes) {
        this.classes = classes;
    }

    /**
     * @return an array of css classes
     */
    public String[] css() {
        return classes;
    }
}
