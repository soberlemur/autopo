package ooo.autopo.app.ui.components;

/*
 * This file is part of the Autopo project
 * Created 18/02/25
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
