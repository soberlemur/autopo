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
    private StageService service;

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
