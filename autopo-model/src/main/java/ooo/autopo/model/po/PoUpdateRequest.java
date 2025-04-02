package ooo.autopo.model.po;

/*
 * This file is part of the Autopo project
 * Created 10/03/25
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

import javafx.beans.property.SimpleBooleanProperty;

import java.util.Collection;
import java.util.List;

import static org.sejda.commons.util.RequireUtils.requireNotNullArg;

/**
 * @author Andrea Vacondio
 */
public record PoUpdateRequest(PotFile potFile, Collection<PoFile> poFiles, SimpleBooleanProperty complete) {

    public PoUpdateRequest {
        requireNotNullArg(potFile, "Cannot update Po files using a null potFile");
    }

    public PoUpdateRequest(PotFile potFile, Collection<PoFile> poFiles) {
        this(potFile, poFiles, new SimpleBooleanProperty(false));
    }

    public PoUpdateRequest(PotFile potFile, PoFile poFile) {
        this(potFile, List.of(poFile), new SimpleBooleanProperty(false));
    }
}