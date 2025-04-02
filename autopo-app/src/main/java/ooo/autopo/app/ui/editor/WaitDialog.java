package ooo.autopo.app.ui.editor;

/*
 * This file is part of the Autopo project
 * Created 20/03/25
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

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

/**
 * @author Andrea Vacondio
 */
public class WaitDialog extends VBox {

    public WaitDialog() {
        getStyleClass().add("autopo-dialog");
        setSpacing(10);
        setAlignment(Pos.CENTER);
        setMinSize(350, 200);
        setMaxSize(350, 200);
        var progress = new ProgressBar();
        progress.setPrefWidth(200);
        getChildren().addAll(new Label(), progress);
    }

    public void setLabel(Label label) {
        getChildren().set(0, label);
    }
}
