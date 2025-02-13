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

import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Event to change the title on the application window.
 *
 * @author Andrea Vacondio
 */
public record SetTitleRequest(String title) {
    public SetTitleRequest(String title) {
        this.title = trim(title);
    }
}
