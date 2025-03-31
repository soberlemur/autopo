package ooo.autopo.model.project;

/*
 * This file is part of the Autopo project
 * Created 21/02/25
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

import static org.sejda.commons.util.RequireUtils.requireNotNullArg;

/**
 * Request to save a project
 *
 * @author Andrea Vacondio
 */
public record ProjectSaveRequest(Project project) {
    public ProjectSaveRequest {
        requireNotNullArg(project, "Cannot save a null project");
    }
}