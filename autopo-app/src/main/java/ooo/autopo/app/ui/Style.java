package ooo.autopo.app.ui;
/*
 * This file is part of the Autopo project
 * Created 13/02/25
 * Copyright 2025 by Sober Lemur S.r.l. (info@soberlemur.com).
 *
 * You are not permitted to distribute it in any form unless explicit
 * consent is given by Sober Lemur S.r.l..
 * You are not permitted to modify it.
 *
 * Autopo is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

/**
 * @author Andrea Vacondio
 */
public enum Style {
    CONTAINER("autopo-container"),
    BUTTON("btn"),
    SAVE_BUTTON("btn", "toolbar-btn", "save-btn"),
    CLOSE_BUTTON("btn", "close-btn"),
    TOOL_PANEL("pdfblack-container", "tool-panel"),
    TOOLBAR_BUTTON("btn", "toolbar-btn"),
    TOOLBAR_BUTTON_RIGHT("btn", "btn-right", "toolbar-btn"),
    TOOLBAR_BUTTON_LEFT("btn", "btn-left", "toolbar-btn"),
    TOOLBAR_BUTTON_CENTER("btn", "btn-center", "toolbar-btn"),
    OVERLAY_BUTTON("btn", "page-tile-overlay-btn"),
    SIDEBAR_BUTTON("btn", "sidebar-button"),
    TOOLBAR("tool-toolbar-buttons");

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
