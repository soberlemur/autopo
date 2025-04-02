package ooo.autopo.app.ui.editor;

/*
 * This file is part of the Autopo project
 * Created 25/02/25
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
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import ooo.autopo.model.po.PoFile;

/**
 * @author Andrea Vacondio
 */

public class EditorPane extends SplitPane {

    private PoFile poFile;

    @Inject
    public EditorPane(TranslationsPane translationsPane, TranslateEntryPanel translationEditPane) {
        this.setOrientation(Orientation.VERTICAL);
        this.setDividerPositions(0.75);
        this.getItems().addAll(translationsPane, translationEditPane);
    }
}
