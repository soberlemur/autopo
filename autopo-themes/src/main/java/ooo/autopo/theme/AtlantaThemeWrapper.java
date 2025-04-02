package ooo.autopo.theme;

/*
 * This file is part of the Autopo project
 * Created 20/02/25
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
