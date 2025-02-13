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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.synedra.validatorfx.DefaultDecoration;
import ooo.autopo.app.config.AppConfig;
import ooo.autopo.app.config.PersistenceConfig;
import ooo.autopo.app.config.ServicesConfig;
import ooo.autopo.app.context.StringPersistentProperty;
import ooo.autopo.app.ui.notification.NotificationsContainer;
import ooo.autopo.app.validation.CompositeDecoration;
import ooo.autopo.i18n.SetLocaleRequest;
import ooo.autopo.model.lifecycle.CleanupRequest;
import ooo.autopo.model.lifecycle.ShutdownEvent;
import ooo.autopo.model.lifecycle.StartupEvent;
import ooo.autopo.model.ui.SetLatestStageStatusRequest;
import ooo.autopo.model.ui.StageMode;
import ooo.autopo.model.ui.StageStatus;
import org.pdfsam.injector.Injector;
import org.pdfsam.persistence.PersistenceException;
import org.tinylog.Logger;

import java.awt.SplashScreen;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static ooo.autopo.app.context.ApplicationContext.APPLICATION_TITLE;
import static ooo.autopo.app.context.ApplicationContext.app;
import static ooo.autopo.i18n.I18nContext.i18n;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
public class AutopoApp extends Application {

    private List<String> rawParameters;
    private boolean clean;
    private Stage primaryStage;

    @Override
    public void init() {
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionLogger());
        rawParameters = getParameters().getRaw();
        Logger.info("Starting...");
        clean = rawParameters.contains("--clean") || rawParameters.contains("-clean") || rawParameters.contains("-c");
        if (clean) {
            try {
                app().clean();
                Logger.info("Persistent application settings deleted");
            } catch (PersistenceException e) {
                Logger.error("Unable to clear application settings", e);
            }
        }
        app().persistentSettings().get(StringPersistentProperty.LOCALE).map(SetLocaleRequest::new).ifPresent(eventStudio()::broadcast);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initInjector(primaryStage);
        cleanIfRequired();
        //  var config = app().instance(AppBrand.class);
        //  System.setProperty(IOUtils.TMP_BUFFER_PREFIX_PROPERTY_NAME, config.property(BrandableProperty.HANDLE, "pdfblack"));
        primaryStage.setScene(initScene());
        primaryStage.setOnCloseRequest(e -> Platform.exit());

        //   primaryStage.getIcons().addAll(config.icons());

        app().instance(WindowStatusController.class).setStage(primaryStage);
        app().instance(ApplicationTitleController.class).setStage(primaryStage);

        DefaultDecoration.setFactory(CompositeDecoration::createCompositeDecoration);
        initStartupContentItem();

        primaryStage.show();
        // keep the scale updated
        // primaryStage.getScene().getWindow().outputScaleXProperty().addListener((a, b, c) -> app().runtimeState().scale(c.doubleValue()));
        closeSplash();
        eventStudio().broadcast(new StartupEvent());
        Logger.info("{} started", APPLICATION_TITLE);
    }

    private void initInjector(Stage primaryStage) {
        Injector.addConfig(new AppConfig(), new ServicesConfig(), new PersistenceConfig());
        app().injector(Injector.start());
    }

    private Scene initScene() {
        var rootStackPane = app().instance(RootStack.class);
        var notifications = app().instance(NotificationsContainer.class);
        StackPane.setAlignment(notifications, Pos.BOTTOM_RIGHT);
        rootStackPane.getChildren().addAll(app().instance(AppContainer.class), notifications);
        var mainScene = new Scene(rootStackPane);
        mainScene.getAccelerators().put(new KeyCodeCombination(KeyCode.Q, KeyCombination.SHORTCUT_DOWN), Platform::exit);
        return mainScene;
    }

    private void initStartupContentItem() {
        //TODO maybe open the latest project
    }

    private void cleanIfRequired() {
        if (clean) {
            Logger.debug("Cleaning...");
            eventStudio().broadcast(new CleanupRequest());
        }
    }

    private void closeSplash() {
        Optional.ofNullable(SplashScreen.getSplashScreen()).ifPresent(SplashScreen::close);
    }

    @Override
    public void stop() {
        Logger.info("{}", i18n().tr("Closing..."));
        if (nonNull(primaryStage)) {
            var status = new StageStatus(this.primaryStage.getX(),
                                         this.primaryStage.getY(),
                                         this.primaryStage.getWidth(),
                                         this.primaryStage.getHeight(),
                                         StageMode.valueFor(this.primaryStage));
            eventStudio().broadcast(new SetLatestStageStatusRequest(status));
        }
        eventStudio().broadcast(new ShutdownEvent());
        app().close();
    }
}
