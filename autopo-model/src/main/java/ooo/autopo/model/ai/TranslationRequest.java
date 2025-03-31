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

import java.util.List;

/**
 * Request to translate the input {@link PoEntry}s
 *
 * @author Andrea Vacondio
 */
public record TranslationRequest(PoFile poFile, List<PoEntry> poEntries, AIModelDescriptor descriptor, String projectDescription,
                                 SimpleBooleanProperty complete) {
    public TranslationRequest(PoFile poFile, List<PoEntry> poEntries, AIModelDescriptor descriptor, String projectDescription) {
        this(poFile, poEntries, descriptor, projectDescription, new SimpleBooleanProperty(false));
    }

    public TranslationRequest(PoFile poFile, PoEntry poEntry, AIModelDescriptor descriptor, String projectDescription) {
        this(poFile, List.of(poEntry), descriptor, projectDescription, new SimpleBooleanProperty(false));
    }
}


