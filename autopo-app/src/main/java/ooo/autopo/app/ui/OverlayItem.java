package ooo.autopo.app.ui;

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

import java.util.function.Supplier;

/**
 * @author Andrea Vacondio
 */
public interface OverlayItem {

    String id();

    String name();

    Supplier<Node> panel();

    /**
     * @return true if the overlay should be closed when a second show request arrives. Ex. the log panel has a button to show it, first click shows the panel,
     * second click hides it.
     */
    default boolean closeOnSecondPress() {
        return false;
    }
}
