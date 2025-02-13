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

import javafx.scene.layout.StackPane;

/**
 * @author Andrea Vacondio
 */
public class RootStack extends StackPane {

    public RootStack() {
        this.setId("autopo-app-root-stack");
        setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
    }
}
