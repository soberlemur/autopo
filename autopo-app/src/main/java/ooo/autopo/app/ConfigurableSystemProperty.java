package ooo.autopo.app;

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
public class ConfigurableSystemProperty {
    public static final String LOCALE_PROP = "ooo.autopo.default.locale";
    public static final String CHECK_FOR_UPDATES_PROP = "ooo.autopo.default.checkforupdate";
    public static final String THEME_PROP = "ooo.autopo.default.theme";

    private ConfigurableSystemProperty() {
        //HIDE
    }
}
