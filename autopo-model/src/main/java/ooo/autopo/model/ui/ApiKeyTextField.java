package ooo.autopo.model.ui;

/*
 * This file is part of the Autopo project
 * Created 21/03/25
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

import atlantafx.base.controls.PasswordTextField;
import javafx.scene.Cursor;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * @author Andrea Vacondio
 */
public class ApiKeyTextField extends PasswordTextField {

    public ApiKeyTextField() {
        setMinWidth(400);
        setPromptText("API KEY");
        var icon = new FontIcon(FluentUiFilledAL.EYE_HIDE_24);
        icon.setCursor(Cursor.HAND);
        icon.setOnMouseClicked(e -> {
            if (getRevealPassword()) {
                icon.setIconCode(FluentUiFilledAL.EYE_HIDE_24);
            } else {
                icon.setIconCode(FluentUiFilledAL.EYE_SHOW_24);
            }
            setRevealPassword(!getRevealPassword());
        });
        setRight(icon);
    }
}
