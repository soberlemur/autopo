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
public class PrimerLight extends AtlantaThemeWrapper {

    public PrimerLight() {
        super(new atlantafx.base.theme.PrimerLight());
    }

    @Override
    public String id() {
        return "UD34F1S9XX45FMQ";
    }

    @Override
    public String name() {
        return i18n().tr("Light");
    }

    @Override
    public boolean isDefault() {
        return true;
    }
}
