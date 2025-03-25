package ooo.autopo.app.ui.editor;

/*
 * This file is part of the Autopo project
 * Created 25/03/25
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
import ooo.autopo.model.ai.AIModelDescriptor;
import ooo.autopo.model.notification.AddNotificationRequest;
import ooo.autopo.model.notification.NotificationType;
import ooo.autopo.model.project.ProjectProperty;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static ooo.autopo.app.context.ApplicationContext.app;
import static ooo.autopo.i18n.I18nContext.i18n;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
abstract class AITranslateButton extends Button {
    public AITranslateButton() {
        setOnAction(e -> {
            var description = ofNullable(app().currentProject().getProperty(ProjectProperty.DESCRIPTION)).orElse("");
            if (isBlank(description)) {
                eventStudio().broadcast(new AddNotificationRequest(NotificationType.WARN,
                                                                   i18n().tr(
                                                                           "Add a project description to give the AI model more context and improve translations accuracy")));
            }
            if (isNull(app().currentPoFile().locale())) {
                eventStudio().broadcast(new AddNotificationRequest(NotificationType.ERROR, i18n().tr("The project must have a target locale to translate to")));
            } else {
                var model = app().translationAIModelDescriptor();
                if (model.isPresent()) {
                    sendTranslationRequest(model.get(), description);
                } else {
                    eventStudio().broadcast(new AddNotificationRequest(NotificationType.ERROR, i18n().tr("Unable to find a usable translation AI model")));

                }

            }
        });
    }

    abstract void sendTranslationRequest(AIModelDescriptor aiModelDescriptor, String description);
}
