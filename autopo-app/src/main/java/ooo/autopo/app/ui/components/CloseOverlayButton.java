package ooo.autopo.app.ui.components;

/*
 * This file is part of the Autopo project
 * Created 17/02/25
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

import atlantafx.base.theme.Styles;
import javafx.scene.control.Button;
import ooo.autopo.model.ui.HideOverlayItem;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.javafx.FontIcon;

import static ooo.autopo.i18n.I18nContext.i18n;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
public class CloseOverlayButton extends Button {
    public CloseOverlayButton() {
        setText(i18n().tr("Close"));
        setGraphic(new FontIcon(FluentUiFilledAL.DISMISS_20));
        getStyleClass().addAll(Styles.SMALL);
        setOnAction(e -> eventStudio().broadcast(HideOverlayItem.INSTANCE));
    }
}
