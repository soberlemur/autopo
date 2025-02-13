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

import javafx.stage.Stage;

/**
 * Possible modes for the Stage
 *
 * @author Andrea Vacondio
 */
public enum StageMode {
    MAXIMIZED {
        @Override
        public void restore(Stage stage) {
            stage.setMaximized(true);
        }
    },
    DEFAULT {
        @Override
        public void restore(Stage stage) {
            // nothing to do
        }
    };

    /**
     * @param stage
     * @return the StageMode for the given stage
     */
    public static StageMode valueFor(Stage stage) {
        if (stage.isMaximized()) {
            return MAXIMIZED;
        }
        return DEFAULT;
    }

    public abstract void restore(Stage stage);
}
