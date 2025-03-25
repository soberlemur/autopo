package ooo.autopo.app;

/*
 * This file is part of the Autopo project
 * Created 21/03/25
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

import atlantafx.base.controls.ModalPane;
import jakarta.inject.Inject;
import javafx.application.Platform;
import javafx.scene.control.Label;
import ooo.autopo.app.ui.editor.WaitDialog;
import ooo.autopo.app.ui.explorer.SelectLanguageDialog;
import ooo.autopo.model.ai.TranslationRequest;
import ooo.autopo.model.po.PoAddRequest;
import ooo.autopo.model.po.PoUpdateRequest;
import org.kordamp.ikonli.fluentui.FluentUiRegularAL;
import org.kordamp.ikonli.javafx.FontIcon;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.injector.Auto;

import java.util.Optional;

import static ooo.autopo.i18n.I18nContext.i18n;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
@Auto
public class ModalPaneController {

    private final ModalPane modalPane;
    private WaitDialog translatingDialog;
    private WaitDialog updatingDialog;
    private WaitDialog addingDialog;
    private SelectLanguageDialog selectLanguageDialog;

    @Inject
    public ModalPaneController(ModalPane modalPane) {
        this.modalPane = modalPane;
        eventStudio().addAnnotatedListeners(this);
    }

    @EventListener(priority = Integer.MIN_VALUE)
    public void onTranslationRequest(TranslationRequest request) {
        translatingDialog = Optional.ofNullable(translatingDialog)
                                    .orElseGet(() -> new WaitDialog(new Label(i18n().tr("Translating..."), new FontIcon(FluentUiRegularAL.BOT_24))));
        modalPane.show(translatingDialog);
        request.complete().subscribe((o, n) -> {
            if (n) {
                Platform.runLater(() -> modalPane.hide(true));
            }
        });
    }

    @EventListener(priority = Integer.MIN_VALUE)
    public void onUpdateRequest(PoUpdateRequest request) {
        updatingDialog = Optional.ofNullable(updatingDialog)
                                 .orElseGet(() -> new WaitDialog(new Label(i18n().tr("Updating..."), new FontIcon(FluentUiRegularAL.ARROW_SYNC_24))));
        modalPane.show(updatingDialog);
        request.complete().subscribe((o, n) -> {
            if (n) {
                Platform.runLater(() -> modalPane.hide(true));
            }
        });
    }

    @EventListener(priority = Integer.MIN_VALUE)
    public void onPoAdd(PoAddRequest request) {
        addingDialog = Optional.ofNullable(addingDialog)
                               .orElseGet(() -> new WaitDialog(new Label(i18n().tr("Adding .po to the project..."),
                                                                         new FontIcon(FluentUiRegularAL.ARROW_SYNC_24))));
        modalPane.show(addingDialog);
        request.complete().subscribe((o, n) -> {
            if (n) {
                Platform.runLater(() -> modalPane.hide(true));
            }
        });
    }

    @EventListener(station = "LANGUAGE_SELECTION_STATION")
    public void onLanguageSelection(PoAddRequest request) {
        selectLanguageDialog = Optional.ofNullable(selectLanguageDialog)
                                       .orElseGet(() -> new SelectLanguageDialog(() -> Platform.runLater(() -> modalPane.hide(true))));
        selectLanguageDialog.currentRequest(request);
        modalPane.show(selectLanguageDialog);
    }

}
