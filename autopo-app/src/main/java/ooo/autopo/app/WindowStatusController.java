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

import jakarta.inject.Inject;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ooo.autopo.model.ui.StageStatus;
import ooo.autopo.service.ui.StageService;
import org.pdfsam.injector.Auto;
import org.tinylog.Logger;

import java.util.Optional;

/**
 * Controller for the Window status
 *
 * @author Andrea Vacondio
 */
@Auto
public class WindowStatusController {
    private Stage stage;
    private final StageService service;

    @Inject
    WindowStatusController(StageService service) {
        this.service = service;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        initUi();
    }

    private void initUi() {
        var latestStatus = service.getLatestStatus();
        if (latestStatus.isPresent() && hasAvailableScreen(latestStatus.get())) {
            restore(latestStatus.get());
            Logger.trace("Stage status restored to {}", latestStatus);
        } else {
            defaultStageStatus();
            Logger.trace("Stage status set to default values");
        }
    }

    private void defaultStageStatus() {
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 4);
        stage.setMaximized(true);
    }

    private void restore(StageStatus latestStatus) {
        stage.setX(latestStatus.x());
        stage.setY(latestStatus.y());
        stage.setWidth(latestStatus.width());
        stage.setHeight(latestStatus.height());

        if (isNotMac()) {
            latestStatus.mode().restore(stage);
        }
    }

    private boolean isNotMac() {
        return !Optional.of(System.getProperty("os.name")).orElse("").toLowerCase().contains("mac");
    }

    private boolean hasAvailableScreen(StageStatus status) {
        return !Screen.getScreensForRectangle(status.x(), status.y(), status.width(), status.height()).isEmpty();
    }
}
