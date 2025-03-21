package ooo.autopo.model.ai;

/*
 * This file is part of the Autopo project
 * Created 20/03/25
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
import ooo.autopo.model.po.PoEntry;
import ooo.autopo.model.po.PoFile;

/**
 * @author Andrea Vacondio
 */
public record TranslationRequest(PoFile poFile, PoEntry poEntry, AIModelDescriptor descriptor, String projectDescription, SimpleBooleanProperty complete) {
    public TranslationRequest(PoFile poFile, PoEntry poEntry, AIModelDescriptor descriptor, String projectDescription) {
        this(poFile, poEntry, descriptor, projectDescription, new SimpleBooleanProperty(false));
    }
}


