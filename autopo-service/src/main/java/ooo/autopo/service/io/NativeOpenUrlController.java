/*
 * This file is part of the PDF Black project
 * Created on 28 mag 2020
 * Copyright 2020 by Sober Lemur S.r.l. (info@soberlemur.com).
 *
 * You are not permitted to distribute it in any form unless explicit
 * consent is given by Sober Lemur S.r.l..
 * You are not permitted to modify it.
 *
 * PDF Black is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package ooo.autopo.service.io;

import jakarta.inject.Inject;
import javafx.application.HostServices;
import ooo.autopo.model.io.NativeOpenUrlRequest;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.injector.Auto;
import org.tinylog.Logger;

import java.io.IOException;

import static java.util.Objects.nonNull;
import static ooo.autopo.i18n.I18nContext.i18n;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * Controller that receives a request to open a URL in the native browser and tries to fulfill the request
 *
 * @author Andrea Vacondio
 */
@Auto
public class NativeOpenUrlController {

    private HostServices services;

    @Inject
    public NativeOpenUrlController(HostServices services) {
        this.services = services;
        eventStudio().addAnnotatedListeners(this);
    }

    @EventListener
    public void openUrl(NativeOpenUrlRequest event) {
        if (nonNull(event.url()) && !event.url().isBlank()) {
            try {
                services.showDocument(event.url());
            } catch (NullPointerException npe) {
                // service delegate can be null but there's no way to check it first, so we have to catch the npe
                fallbackOpen(event);
            }
        }
    }

    private void fallbackOpen(NativeOpenUrlRequest event) {
        Logger.info("Unable to open url using HostServices, trying fallback");
        try {
            Runtime.getRuntime().exec(getOpenCmd(event.url()));
        } catch (IOException e) {
            Logger.warn(e, i18n().tr("Unable to open '{}', please copy and paste the url to your browser", event.url()));
        }
    }

    private static String[] getOpenCmd(String url) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac")) {
            return new String[] { "open", url };
        }
        if (os.contains("win")) {
            return new String[] { "explorer", url };
        }
        if (os.contains("nix") || os.contains("nux") || os.indexOf("aix") > 0) {
            return new String[] { "xdg-open", url };
        }
        throw new IOException("Unable to identify the open command for the OS " + os);
    }
}
