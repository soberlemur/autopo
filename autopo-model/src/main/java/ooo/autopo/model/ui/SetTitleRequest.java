package ooo.autopo.model.ui;
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

import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Event to change the title on the application window.
 *
 * @author Andrea Vacondio
 */
public record SetTitleRequest(String title) {

    public static final SetTitleRequest SET_DEFAULT_TITLE_REQUEST = new SetTitleRequest("");

    public SetTitleRequest(String title) {
        this.title = trim(title);
    }
}
