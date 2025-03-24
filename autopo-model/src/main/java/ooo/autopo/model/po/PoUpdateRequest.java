package ooo.autopo.model.po;

/*
 * This file is part of the Autopo project
 * Created 10/03/25
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

/**
 * @author Andrea Vacondio
 */
public record PoUpdateRequest(PoFile poFile, PotFile potFile, SimpleBooleanProperty complete) {
    public PoUpdateRequest(PoFile poFile, PotFile potFile) {
        this(poFile, potFile, new SimpleBooleanProperty(false));
    }
}