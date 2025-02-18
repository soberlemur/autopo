package ooo.autopo.app.ui.logs;
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

import org.apache.commons.lang3.ObjectUtils;

/**
 * Model for a Log message
 *
 * @author Andrea Vacondio
 */
record LogMessage(String message, LogLevel level) {

    LogMessage(String message, LogLevel level) {
        this.message = message;
        this.level = ObjectUtils.defaultIfNull(level, LogLevel.INFO);
    }

    @Override
    public String toString() {
        return message();
    }

}
