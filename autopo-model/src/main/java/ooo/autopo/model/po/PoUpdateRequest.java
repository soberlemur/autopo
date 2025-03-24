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

import java.util.Collection;
import java.util.List;

/**
 * @author Andrea Vacondio
 */
public record PoUpdateRequest(PotFile potFile, Collection<PoFile> poFiles, SimpleBooleanProperty complete) {

    public PoUpdateRequest(PotFile potFile, Collection<PoFile> poFiles) {
        this(potFile, poFiles, new SimpleBooleanProperty(false));
    }

    public PoUpdateRequest(PotFile potFile, PoFile poFile) {
        this(potFile, List.of(poFile), new SimpleBooleanProperty(false));
    }
}