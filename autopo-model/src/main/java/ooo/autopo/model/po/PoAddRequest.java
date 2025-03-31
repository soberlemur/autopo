package ooo.autopo.model.po;

/*
 * This file is part of the Autopo project
 * Created 24/03/25
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

import javafx.beans.property.SimpleBooleanProperty;
import ooo.autopo.model.project.Project;

import static org.sejda.commons.util.RequireUtils.requireNotNullArg;

/**
 * Request to add a new Po file to the project
 *
 * @author Andrea Vacondio
 */
public record PoAddRequest(Project project, PoFile poFile, SimpleBooleanProperty complete) {
    public PoAddRequest {
        requireNotNullArg(project, "Cannot add a po file to a null project");
        requireNotNullArg(poFile, "Cannot add a null po file to a project");
    }

    public PoAddRequest(Project project, PoFile poFile) {
        this(project, poFile, new SimpleBooleanProperty(false));
    }
}
