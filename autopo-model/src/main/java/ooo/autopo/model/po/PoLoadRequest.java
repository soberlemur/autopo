package ooo.autopo.model.po;

/*
 * This file is part of the Autopo project
 * Created 05/02/25
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

import ooo.autopo.model.ai.AIModelDescriptor;

import static org.sejda.commons.util.RequireUtils.requireNotNullArg;

/**
 * Request to load the given {@link PoFile}. The background flag signals that the file is not the one requested in the UI and it can be loaded in background
 *
 * @author Andrea Vacondio
 */
public record PoLoadRequest(PoFile poFile, AIModelDescriptor descriptor, boolean background) {

    public PoLoadRequest {
        requireNotNullArg(poFile, "Cannot load a null poFile");
    }

    public PoLoadRequest(PoFile poFile, AIModelDescriptor descriptor) {
        this(poFile, descriptor, false);
    }

}