package ooo.autopo.model.po;

/*
 * This file is part of the Autopo project
 * Created 05/02/25
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