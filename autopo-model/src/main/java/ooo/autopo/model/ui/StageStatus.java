package ooo.autopo.model.ui;
/*
 * This file is part of the Autopo project
 * Created 13/02/25
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

import static java.util.Objects.requireNonNull;

/**
 * Holds data regarding the status of the main stage
 *
 * @author Andrea Vacondio
 */
public record StageStatus(double x, double y, double width, double height, StageMode mode) {
    public StageStatus {
        requireNonNull(mode);
    }

    public StageStatus() {
        this(0, 0, 0, 0, StageMode.DEFAULT);
    }
}
