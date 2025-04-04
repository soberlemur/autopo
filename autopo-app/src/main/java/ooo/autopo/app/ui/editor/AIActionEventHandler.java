package ooo.autopo.app.ui.editor;

/*
 * This file is part of the Autopo project
 * Created 28/03/25
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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import ooo.autopo.model.ai.AIModelDescriptor;
import ooo.autopo.model.notification.AddNotificationRequest;
import ooo.autopo.model.notification.NotificationType;
import ooo.autopo.model.project.ProjectProperty;

import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static ooo.autopo.app.context.ApplicationContext.app;
import static ooo.autopo.i18n.I18nContext.i18n;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
abstract class AIActionEventHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent event) {
        var description = ofNullable(app().currentProject().getProperty(ProjectProperty.DESCRIPTION)).orElse("");
        if (isBlank(description)) {
            eventStudio().broadcast(new AddNotificationRequest(NotificationType.WARN,
                                                               i18n().tr(
                                                                       "Add a project description to give the AI model more context and improve translations accuracy")));
        }
        if (isNull(app().currentPoFile().locale().get())) {
            eventStudio().broadcast(new AddNotificationRequest(NotificationType.ERROR, i18n().tr("The project must have a target locale to translate to")));
        } else {
            Optional<AIModelDescriptor> model = getModel();
            if (model.isPresent()) {
                onPositiveAction(model.get(), description);
            } else {
                eventStudio().broadcast(new AddNotificationRequest(NotificationType.ERROR, i18n().tr("Unable to find a usable translation AI model")));

            }
        }
    }

    abstract void onPositiveAction(AIModelDescriptor aiModelDescriptor, String description);

    Optional<AIModelDescriptor> getModel() {
        return app().translationAIModelDescriptor();
    }

}
