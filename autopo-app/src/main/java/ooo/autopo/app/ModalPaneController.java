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
import ooo.autopo.app.ui.editor.PoUpdatingDialog;
import ooo.autopo.app.ui.editor.TranslatingDialog;
import ooo.autopo.model.ai.TranslationRequest;
import ooo.autopo.model.po.PoUpdateRequest;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.injector.Auto;

import java.util.Optional;

import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
@Auto
public class ModalPaneController {

    private final ModalPane modalPane;
    private TranslatingDialog translatingDialog;
    private PoUpdatingDialog updatingDialog;

    @Inject
    public ModalPaneController(ModalPane modalPane) {
        this.modalPane = modalPane;
        eventStudio().addAnnotatedListeners(this);
    }

    @EventListener(priority = Integer.MIN_VALUE)
    public void onTranslationRequest(TranslationRequest request) {
        translatingDialog = Optional.ofNullable(translatingDialog).orElseGet(TranslatingDialog::new);
        modalPane.show(translatingDialog);
        request.complete().subscribe((o, n) -> {
            if (n) {
                Platform.runLater(() -> modalPane.hide(true));
            }
        });
    }

    @EventListener(priority = Integer.MIN_VALUE)
    public void onUpdateRequest(PoUpdateRequest request) {
        updatingDialog = Optional.ofNullable(updatingDialog).orElseGet(PoUpdatingDialog::new);
        modalPane.show(updatingDialog);
        request.complete().subscribe((o, n) -> {
            if (n) {
                Platform.runLater(() -> modalPane.hide(true));
            }
        });
    }
}
