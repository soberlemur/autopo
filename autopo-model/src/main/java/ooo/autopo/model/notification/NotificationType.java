package ooo.autopo.model.notification;
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

import javafx.scene.Node;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.fluentui.FluentUiFilledMZ;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * @author Andrea Vacondio
 */
public enum NotificationType {
    INFO {
        @Override
        public Node getGraphic() {
            return new FontIcon(FluentUiFilledAL.INFO_20);
        }

        @Override
        public String getStyleClass() {
            return "notification-info";
        }
    },
    WARN {
        @Override
        public Node getGraphic() {
            return new FontIcon(FluentUiFilledMZ.WARNING_20);
        }

        @Override
        public String getStyleClass() {
            return "notification-warn";
        }
    },
    ERROR {
        @Override
        public Node getGraphic() {
            return FontIcon.of(FluentUiFilledAL.DISMISS_20);
        }

        @Override
        public String getStyleClass() {
            return "notification-error";
        }
    };

    public abstract Node getGraphic();

    public abstract String getStyleClass();
}
