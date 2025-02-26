package ooo.autopo.app.ui.editor;

/*
 * This file is part of the Autopo project
 * Created 25/02/25
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

import atlantafx.base.theme.Styles;
import jakarta.inject.Inject;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import ooo.autopo.model.po.PoFile;
import ooo.autopo.model.ui.TranslationsCountChanged;
import ooo.autopo.model.ui.log.SaveLogRequest;
import org.kordamp.ikonli.fluentui.FluentUiRegularMZ;
import org.kordamp.ikonli.javafx.FontIcon;
import org.pdfsam.eventstudio.annotation.EventListener;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static ooo.autopo.app.context.ApplicationContext.app;
import static ooo.autopo.i18n.I18nContext.i18n;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
public class TranslationsTableToolbar extends ToolBar {

    private final Label status = new Label();

    @Inject
    public TranslationsTableToolbar() {
        this.getStyleClass().add("translations-toolbar");
        var saveItem = new TranslationsTableToolbar.SaveButton();
        var spacer = new Pane();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        app().runtimeState().poFile().subscribe(poFile -> {
            if (isNull(poFile)) {
                status.setText("");
            }
        });
        getItems().addAll(saveItem, spacer, status);
        eventStudio().addAnnotatedListeners(this);
    }

    @EventListener
    public void onTranslationsChanged(TranslationsCountChanged e) {
        ofNullable(app().currentPoFile()).map(PoFile::entries).ifPresentOrElse(po -> {
            int entries = 0;
            int translated = 0;
            for (var entry : po) {
                entries++;
                if (isNotBlank(entry.translatedValue().get())) {
                    translated++;
                }
            }
            status.setText(i18n().tr("Translated {0} out of {1}", Integer.toString(translated), Integer.toString(entries)));

        }, () -> status.setText(""));
    }

    static class SaveButton extends Button {
        public SaveButton() {
            setText(i18n().tr("_Save"));
            setGraphic(new FontIcon(FluentUiRegularMZ.SAVE_20));
            getStyleClass().addAll(Styles.SMALL);
            //TODO
            setOnAction(e -> eventStudio().broadcast(new SaveLogRequest()));
        }
    }
}
