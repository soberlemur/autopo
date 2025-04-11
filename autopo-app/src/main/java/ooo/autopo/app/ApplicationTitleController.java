package ooo.autopo.app;
/*
 * This file is part of the Autopo project
 * Created 13/02/25
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

import javafx.stage.Stage;
import ooo.autopo.model.ui.SetTitleRequest;
import org.apache.commons.lang3.StringUtils;
import org.pdfsam.injector.Auto;

import java.util.Locale;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ooo.autopo.app.context.ApplicationContext.APPLICATION_TITLE;
import static ooo.autopo.app.context.ApplicationContext.app;
import static ooo.autopo.i18n.I18nContext.i18n;
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
            } else {
                var title = ofNullable(app().currentPoFile()).map(p -> p.locale().get())
                                                             .map(Locale::getDisplayName)
                                                             .map(StringUtils::capitalize)
                                                             .map(l -> APPLICATION_TITLE + " - " + l)
                                                             .orElse(APPLICATION_TITLE);
                if (nonNull(app().currentPoFile()) && app().currentPoFile().modifiedProperty().get()) {
                    title = title + " (" + i18n().tr("modified") + ")";
                }
                primaryStage.setTitle(title);
            }
        });
    }

}
