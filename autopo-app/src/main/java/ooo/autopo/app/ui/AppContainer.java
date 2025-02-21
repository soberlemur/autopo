package ooo.autopo.app.ui;

/*
 * This file is part of the Autopo project
 * Created 14/02/25
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

import jakarta.inject.Inject;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import ooo.autopo.app.ui.components.ClosablePane;
import ooo.autopo.app.ui.components.NotClosable;

import java.util.Objects;

/**
 * @author Andrea Vacondio
 */
public class AppContainer extends BorderPane {

    private final MainPane mainPanel;

    @Inject
    public AppContainer(MainMenuBar menu, MainPane mainPanel, FooterBar footer) {
        setId("app-container");
        this.mainPanel = mainPanel;
        setCenter(mainPanel);
        setTop(menu);
        setBottom(footer);
    }

    public void overlay(Node overlay) {
        if (Objects.nonNull(overlay)) {
            if (overlay instanceof NotClosable) {
                setCenter(overlay);
            } else {
                setCenter(new ClosablePane(overlay));
            }
        }
    }

    public void hideOverlay() {
        setCenter(mainPanel);
    }

}
