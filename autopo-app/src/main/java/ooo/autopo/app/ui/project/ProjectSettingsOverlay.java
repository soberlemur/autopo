package ooo.autopo.app.ui.project;

/*
 * This file is part of the Autopo project
 * Created 19/03/25
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
import ooo.autopo.app.ui.OverlayItem;

import java.util.function.Supplier;

import static ooo.autopo.app.context.ApplicationContext.app;
import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * @author Andrea Vacondio
 */
public class ProjectSettingsOverlay implements OverlayItem {
    @Override
    public String id() {
        return "PROJECT_SETTINGS";
    }

    @Override
    public String name() {
        return i18n().tr("Project settings");
    }

    @Override
    public Supplier<Node> panel() {
        return () -> app().instance(ProjectSettingsPane.class);
    }
}
