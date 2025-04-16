package ooo.autopo.model;

/*
 * This file is part of the Autopo project
 * Created 14/02/25
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
public enum AppDescriptorProperty {
    NAME("ooo.autopo.name"),
    HOME_URL("ooo.autopo.home.url"),
    HOME_LABEL("ooo.autopo.home.label"),
    VERSION("ooo.autopo.version"),
    COPYRIGHT("ooo.autopo.copyright"),
    VENDOR_URL("ooo.autopo.vendor.url"),
    LICENSE_NAME("ooo.autopo.license.name"),
    LICENSE_URL("ooo.autopo.license.url"),
    TRACKER_URL("ooo.autopo.tracker.url"),
    SCM_URL("ooo.autopo.scm.url");
    public final String prop;

    AppDescriptorProperty(String prop) {
        this.prop = prop;
    }
}
