package ooo.autopo.model.ai;

/*
 * This file is part of the Autopo project
 * Created 26/03/25
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
import ooo.autopo.model.po.PoEntry;
import ooo.autopo.model.po.PoFile;

import java.util.List;

/**
 * Request to assess/validate the translations in the input {@link PoEntry}s
 *
 * @author Andrea Vacondio
 */
public record AssessmentRequest(PoFile poFile, List<PoEntry> poEntries, AIModelDescriptor descriptor, String projectDescription,
                                SimpleBooleanProperty complete) {
    public AssessmentRequest(PoFile poFile, List<PoEntry> poEntries, AIModelDescriptor descriptor, String projectDescription) {
        this(poFile, poEntries, descriptor, projectDescription, new SimpleBooleanProperty(false));
    }

    public AssessmentRequest(PoFile poFile, PoEntry poEntry, AIModelDescriptor descriptor, String projectDescription) {
        this(poFile, List.of(poEntry), descriptor, projectDescription, new SimpleBooleanProperty(false));
    }
}