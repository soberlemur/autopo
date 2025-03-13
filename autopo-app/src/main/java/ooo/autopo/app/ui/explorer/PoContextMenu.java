package ooo.autopo.app.ui.explorer;

/*
 * This file is part of the Autopo project
 * Created 09/03/25
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

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import ooo.autopo.model.LoadingStatus;
import ooo.autopo.model.po.PoFile;
import ooo.autopo.model.po.PoSaveRequest;
import ooo.autopo.model.ui.SetStatusLabelRequest;

import static javafx.beans.binding.Bindings.not;
import static javafx.beans.binding.Bindings.notEqual;
import static ooo.autopo.app.context.ApplicationContext.app;
import static ooo.autopo.i18n.I18nContext.i18n;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;

/**
 * @author Andrea Vacondio
 */
public class PoContextMenu extends ContextMenu {
    private final PoFile poFile;

    public PoContextMenu(PoFile poFile) {
        this.poFile = poFile;
        var editPo = new MenuItem(i18n().tr("Edit"));
        editPo.setOnAction(e -> app().runtimeState().poFile(poFile));

        var savePo = new MenuItem(i18n().tr("Save"));
        savePo.disableProperty().bind(not(poFile.modifiedProperty()));
        savePo.setOnAction(e -> eventStudio().broadcast(new PoSaveRequest(poFile)));

        var clearObsolete = new MenuItem(i18n().tr("Remove obsolete entries"));
        clearObsolete.setOnAction(e -> {
            var message = i18n().tr("No obsolete entries to remove");
            if (poFile.clearObsolete()) {
                message = i18n().tr("Obsolete entries removed");
            }
            eventStudio().broadcast(new SetStatusLabelRequest(message));
        });
        clearObsolete.disableProperty().bind(notEqual(poFile.status(), LoadingStatus.LOADED));

        this.getItems().addAll(editPo, savePo, clearObsolete);
    }
}
