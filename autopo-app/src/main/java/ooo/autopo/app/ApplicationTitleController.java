package ooo.autopo.app;
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

import javafx.stage.Stage;
import ooo.autopo.model.ui.SetTitleRequest;
import org.pdfsam.injector.Auto;

import static ooo.autopo.app.context.ApplicationContext.APPLICATION_TITLE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
@Auto
public class ApplicationTitleController {

    public void setStage(Stage primaryStage) {
        primaryStage.setTitle(APPLICATION_TITLE);
        eventStudio().add(SetTitleRequest.class, request -> {
            if (isNotBlank(request.title())) {
                primaryStage.setTitle(APPLICATION_TITLE + " - " + request.title());
            }
        });
    }

}
