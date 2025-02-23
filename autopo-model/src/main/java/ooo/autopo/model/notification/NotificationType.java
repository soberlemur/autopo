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
import org.kordamp.ikonli.fluentui.FluentUiRegularAL;
import org.kordamp.ikonli.fluentui.FluentUiRegularMZ;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * @author Andrea Vacondio
 */
public enum NotificationType {
    INFO {
        @Override
        public Node getGraphic() {
            return new FontIcon(FluentUiRegularAL.INFO_20);
        }
    },
    WARN {
        @Override
        public Node getGraphic() {
            return new FontIcon(FluentUiRegularMZ.WARNING_20);
        }
    },
    ERROR {
        @Override
        public Node getGraphic() {
            return FontIcon.of(FluentUiRegularAL.ERROR_CIRCLE_20);
        }
    };

    public abstract Node getGraphic();

}
