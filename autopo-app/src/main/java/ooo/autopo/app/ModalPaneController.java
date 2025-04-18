package ooo.autopo.app;

/*
 * This file is part of the Autopo project
 * Created 21/03/25
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

import atlantafx.base.controls.ModalPane;
import jakarta.inject.Inject;
import javafx.application.Platform;
import javafx.scene.control.Label;
import ooo.autopo.app.ui.editor.WaitDialog;
import ooo.autopo.app.ui.explorer.SelectLanguageDialog;
import ooo.autopo.model.ai.AssessmentRequest;
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
    private WaitDialog waitDialog = new WaitDialog();
    private SelectLanguageDialog selectLanguageDialog;

    @Inject
    public ModalPaneController(ModalPane modalPane) {
        this.modalPane = modalPane;
        eventStudio().addAnnotatedListeners(this);
    }

    @EventListener(priority = Integer.MIN_VALUE)
    public void onTranslationRequest(TranslationRequest request) {
        waitDialog.setLabel(new Label(i18n().tr("Translating..."), new FontIcon(FluentUiRegularAL.BOT_24)));
        modalPane.show(waitDialog);
        request.complete().subscribe((o, n) -> {
            if (n) {
                Platform.runLater(() -> modalPane.hide(true));
            }
        });
    }

    @EventListener(priority = Integer.MIN_VALUE)
    public void onUpdateRequest(PoUpdateRequest request) {
        waitDialog.setLabel(new Label(i18n().tr("Updating..."), new FontIcon(FluentUiRegularAL.ARROW_SYNC_24)));
        modalPane.show(waitDialog);
        request.complete().subscribe((o, n) -> {
            if (n) {
                Platform.runLater(() -> modalPane.hide(true));
            }
        });
    }

    @EventListener(priority = Integer.MIN_VALUE)
    public void onPoAdd(PoAddRequest request) {
        waitDialog.setLabel(new Label(i18n().tr("Adding .po to the project..."), new FontIcon(FluentUiRegularAL.ARROW_SYNC_24)));
        modalPane.show(waitDialog);
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

    @EventListener(priority = Integer.MIN_VALUE)
    public void onAssessmentRequest(AssessmentRequest request) {
        waitDialog.setLabel(new Label(i18n().tr("Validating..."), new FontIcon(FluentUiRegularAL.BOT_24)));
        modalPane.show(waitDialog);
        request.complete().subscribe((o, n) -> {
            if (n) {
                Platform.runLater(() -> modalPane.hide(true));
            }
        });
    }

}
