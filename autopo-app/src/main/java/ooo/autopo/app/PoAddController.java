package ooo.autopo.app;

/*
 * This file is part of the Autopo project
 * Created 07/04/25
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

import com.soberlemur.potentilla.Catalog;
import javafx.stage.Stage;
import ooo.autopo.app.io.Choosers;
import ooo.autopo.model.io.FileType;
import ooo.autopo.model.notification.AddNotificationRequest;
import ooo.autopo.model.notification.NotificationType;
import ooo.autopo.model.po.PoAddRequest;
import ooo.autopo.model.po.PoAddRequestBuildRequest;
import ooo.autopo.model.po.PoFile;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.injector.Auto;

import java.nio.file.Files;

import static java.util.Objects.nonNull;
import static ooo.autopo.app.context.ApplicationContext.app;
import static ooo.autopo.i18n.I18nContext.i18n;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
@Auto
public class PoAddController {

    private Stage stage;

    public PoAddController() {
        eventStudio().addAnnotatedListeners(this);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @EventListener
    public void onPoAddRequested(PoAddRequestBuildRequest request) {
        var poPath = Choosers.fileChooser(i18n().tr("Save a .po translation file"), FileType.PO).showSaveDialog(stage);
        if (nonNull(poPath)) {
            var filename = poPath.getFileName().toString();
            if (!filename.toLowerCase().endsWith(".po")) {
                poPath = poPath.resolveSibling(filename + ".po");
            }
            if (Files.exists(poPath)) {
                eventStudio().broadcast(new AddNotificationRequest(NotificationType.ERROR,
                                                                   i18n().tr("The file {0} already exists", poPath.getFileName().toString())));
            } else if (!poPath.toAbsolutePath().startsWith(app().currentProject().location().toAbsolutePath())) {
                eventStudio().broadcast(new AddNotificationRequest(NotificationType.ERROR,
                                                                   i18n().tr("The file {0} is not inside the current project directory",
                                                                             poPath.getFileName().toString())));
            } else {
                var poFile = new PoFile(poPath);
                poFile.catalog(new Catalog().withDefaultHeader());
                eventStudio().broadcast(new PoAddRequest(app().currentProject(), poFile), "LANGUAGE_SELECTION_STATION");
            }
        }
    }
}
