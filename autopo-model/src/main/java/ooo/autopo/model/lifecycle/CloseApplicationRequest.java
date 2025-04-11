package ooo.autopo.model.lifecycle;

/*
 * This file is part of the Autopo project
 * Created 11/04/25
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
 * Request to close application
 *
 * @author Andrea Vacondio
 */
public class CloseApplicationRequest {
    public static final CloseApplicationRequest INSTANCE = new CloseApplicationRequest();

    private CloseApplicationRequest() {
        //NOOP
    }
}
