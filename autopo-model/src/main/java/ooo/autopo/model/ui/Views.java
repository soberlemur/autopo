package ooo.autopo.model.ui;

/*
 * This file is part of the Autopo project
 * Created 18/02/25
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

import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.kordamp.ikonli.fluentui.FluentUiRegularMZ;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * @author Andrea Vacondio
 */
public class Views {

    public static Text helpIcon(String text) {
        var tooltip = new Tooltip(text);
        var icon = FontIcon.of(FluentUiRegularMZ.QUESTION_CIRCLE_20);
        icon.getStyleClass().add("help-icon");
        tooltip.setHideDelay(Duration.millis(100));
        tooltip.setShowDelay(Duration.millis(0));
        tooltip.setShowDuration(Duration.seconds(10));
        Tooltip.install(icon, tooltip);
        return icon;
    }
}
