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

import org.tinylog.Level;

/**
 * Recognized log levels.
 *
 * @author Andrea Vacondio
 */
enum LogLevel {
    INFO {
        @Override
        public String style() {
            return "info-log";
        }
    },
    WARN {
        @Override
        public String style() {
            return "warn-log";
        }
    },
    ERROR {
        @Override
        public String style() {
            return "error-log";
        }
    };

    public abstract String style();

    public static LogLevel toLogLevel(Level intLevel) {
        return switch (intLevel) {
            case ERROR -> ERROR;
            case WARN -> WARN;
            default -> INFO;
        };
    }

}
