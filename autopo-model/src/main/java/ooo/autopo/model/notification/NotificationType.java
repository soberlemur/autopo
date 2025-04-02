package ooo.autopo.model.notification;
/*
 * This file is part of the Autopo project
 * Created 13/02/25
 * Copyright 2025 by Sober Lemur S.r.l. (info@soberlemur.com).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
