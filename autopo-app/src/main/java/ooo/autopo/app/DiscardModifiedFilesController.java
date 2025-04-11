package ooo.autopo.app;

/*
 * This file is part of the Autopo project
 * Created 11/04/25
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

import javafx.application.Platform;
import javafx.scene.control.ButtonBar;
import javafx.stage.Stage;
import ooo.autopo.app.ui.DiscardModifiedPoFilesConfirmationDialog;
import ooo.autopo.model.lifecycle.CloseApplicationRequest;
import ooo.autopo.model.project.Project;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.injector.Auto;

import java.util.Collections;

import static java.util.Optional.ofNullable;
import static ooo.autopo.app.context.ApplicationContext.app;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * Controller responsible for intercepting requests thatrequire user confirmation because of pending modifications
 *
 * @author Andrea Vacondio
 */
@Auto
public class DiscardModifiedFilesController {

    private Stage stage;

    public DiscardModifiedFilesController() {
        eventStudio().addAnnotatedListeners(this);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @EventListener(priority = Integer.MIN_VALUE)
    public void onClose(CloseApplicationRequest request) {
        var translations = ofNullable(app().currentProject()).map(Project::translations).orElse(Collections.emptySortedSet());
        var hasModified = translations.stream().anyMatch(poFile -> poFile.modifiedProperty().get());
        if (hasModified) {
            var dialog = new DiscardModifiedPoFilesConfirmationDialog();
            dialog.initOwner(stage);
            if (dialog.showAndWait().filter(b -> b.getButtonData() == ButtonBar.ButtonData.YES).isPresent()) {
                Platform.exit();
            }
        } else {
            Platform.exit();
        }
    }
}
