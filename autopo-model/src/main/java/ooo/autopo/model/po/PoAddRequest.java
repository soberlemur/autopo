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

/**
 * @author Andrea Vacondio
 */
public record PoAddRequest(Project project, PoFile poFile, SimpleBooleanProperty complete) {
    public PoAddRequest(Project project, PoFile poFile) {
        this(project, poFile, new SimpleBooleanProperty(false));
    }
}
