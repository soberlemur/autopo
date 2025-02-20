package ooo.autopo.theme;

/*
 * This file is part of the Autopo project
 * Created 20/02/25
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

import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * @author Andrea Vacondio
 */
public class Dracula extends AtlantaThemeWrapper {

    public Dracula() {
        super(new atlantafx.base.theme.Dracula());
    }

    @Override
    public String id() {
        return "L3W12ZM7UQ13G";
    }

    @Override
    public String name() {
        return i18n().tr("Darcula");
    }
}
