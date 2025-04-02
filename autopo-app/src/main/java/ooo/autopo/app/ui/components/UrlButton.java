package ooo.autopo.app.ui.components;
/*
 * This file is part of the Autopo project
 * Created 14/02/25
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

import javafx.scene.control.Button;
import ooo.autopo.app.ui.Style;
import ooo.autopo.model.io.NativeOpenUrlRequest;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

import static java.util.Objects.nonNull;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;
import static org.sejda.commons.util.RequireUtils.requireNotBlank;

/**
 * Button that when clicked sends a {@link NativeOpenUrlRequest} to open natively the given url
 *
 * @author Andrea Vacondio
 */
public class UrlButton extends Button {

    private UrlButton(String text) {
        super(text);
    }

    /**
     * factory methods to create an url button with default {@link Style#BUTTON} style
     *
     * @param text optional button text
     * @param url
     * @param icon optional icon
     */
    public static UrlButton styledUrlButton(String text, String url, Ikon icon) {
        return urlButton(text, url, icon, Style.BUTTON.css());
    }

    /**
     * Factory method to create an {@link UrlButton}
     *
     * @param text  optional button text
     * @param url
     * @param icon  optional icon
     * @param style optional style classes
     */
    public static UrlButton urlButton(String text, String url, Ikon icon, String... style) {
        requireNotBlank(url, "URL cannot be blank");
        UrlButton button = new UrlButton(text);
        button.setOnAction(e -> eventStudio().broadcast(new NativeOpenUrlRequest(url)));
        if (nonNull(icon)) {
            button.setGraphic(FontIcon.of(icon));
        }
        if (nonNull(style) && style.length > 0) {
            button.getStyleClass().addAll(style);
        }
        return button;
    }
}
