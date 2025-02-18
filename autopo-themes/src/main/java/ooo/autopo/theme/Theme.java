package ooo.autopo.theme;
/*
 * This file is part of the Autopo project
 * Created 17/02/25
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
     * @return a collection of stylesheets paths
     */
    List<String> stylesheets();

    /**
     * @return a collection of stylesheets paths to be loaded if the platform is transparent incapable
     */
    List<String> transparentIncapableStylesheets();

    /**
     * @return The theme name
     */
    String name();

    /**
     * @return if the theme is a dark theme
     */
    boolean isDark();
}
