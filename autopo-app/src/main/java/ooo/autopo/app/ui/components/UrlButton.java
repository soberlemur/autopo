package ooo.autopo.app.ui.components;
/*
 * This file is part of the Autopo project
 * Created 14/02/25
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
