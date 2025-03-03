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
    public EditorPane(TranslationsPane translationsPane, TranslationEditPane translationEditPane) {
        this.setOrientation(Orientation.VERTICAL);
        this.setDividerPositions(0.75);
        this.getItems().addAll(translationsPane, translationEditPane);
    }
}
