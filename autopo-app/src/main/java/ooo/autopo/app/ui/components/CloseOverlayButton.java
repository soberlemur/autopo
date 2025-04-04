package ooo.autopo.app.ui.components;

/*
 * This file is part of the Autopo project
 * Created 17/02/25
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

import atlantafx.base.theme.Styles;
import javafx.scene.control.Button;
import ooo.autopo.model.ui.HideOverlayItemRequest;
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
        setOnAction(e -> eventStudio().broadcast(HideOverlayItemRequest.INSTANCE));
    }
}
