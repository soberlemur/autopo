package ooo.autopo.theme;

/*
 * This file is part of the Autopo project
 * Created 20/02/25
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
import java.util.Objects;

/**
 * @author Andrea Vacondio
 */
abstract class AtlantaThemeWrapper implements Theme {

    private final atlantafx.base.theme.Theme theme;

    public AtlantaThemeWrapper(atlantafx.base.theme.Theme theme) {
        this.theme = Objects.requireNonNull(theme);
    }

    @Override
    public List<String> stylesheets() {
        return List.of("/themes/base/theme.css", "/themes/base/theme.last.css");
    }

    @Override
    public String transparentIncapableStylesheets() {
        return "/themes/base/transparent-incapable.css";
    }

    @Override
    public String name() {
        return this.theme.getName();
    }

    @Override
    public boolean isDark() {
        return theme.isDarkMode();
    }

    @Override
    public boolean isDefault() {
        return false;
    }

    @Override
    public String getUserAgentStylesheet() {
        return theme.getUserAgentStylesheet();
    }
}
