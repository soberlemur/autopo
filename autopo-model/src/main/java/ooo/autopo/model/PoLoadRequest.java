package ooo.autopo.model;

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

import static org.sejda.commons.util.RequireUtils.requireNotNullArg;

/**
 * @author Andrea Vacondio
 */
public record PoLoadRequest(PoFile poFile) {

    public PoLoadRequest {
        requireNotNullArg(poFile, "Cannot load a null poFile");
    }

}