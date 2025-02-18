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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import ooo.autopo.app.ui.components.ClosablePane;

import java.util.Objects;

/**
 * @author Andrea Vacondio
 */
public class AppContainer extends BorderPane {

    private final ScrollPane center = new ScrollPane();
    private final MainPane mainPanel;
    private Node overlay;

    @Inject
    public AppContainer(MainMenuBar menu, MainPane mainPanel, FooterBar footer) {
        setId("app-container");
        this.mainPanel = mainPanel;
        center.getStyleClass().addAll(Style.CONTAINER.css());
        center.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        center.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        center.setFitToWidth(true);
        center.setFitToHeight(true);
        center.setContent(mainPanel);
        setCenter(center);
        setTop(menu);
        setBottom(footer);
    }

    public void overlay(Node overlay) {
        if (Objects.nonNull(overlay)) {
            this.overlay = overlay;
            center.setContent(new ClosablePane(overlay));
        }
    }

    public void hideOverlay() {
        this.overlay = null;
        center.setContent(mainPanel);
    }

}
