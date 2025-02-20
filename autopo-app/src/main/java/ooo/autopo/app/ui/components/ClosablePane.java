package ooo.autopo.app.ui.components;

/*
 * This file is part of the Autopo project
 * Created 17/02/25
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

import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;

/**
 * @author Andrea Vacondio
 */
public class ClosablePane extends BorderPane implements NotClosable {

    public ClosablePane(Node toClose) {
        this.setTop(new ToolBar(new CloseOverlayButton()));
        this.setCenter(toClose);
    }
}
