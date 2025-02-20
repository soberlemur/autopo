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
public class NordLight extends AtlantaThemeWrapper {

    public NordLight() {
        super(new atlantafx.base.theme.NordLight());
    }

    @Override
    public String id() {
        return "II2L0L08A1AQ4NNR5";
    }

    @Override
    public String name() {
        return i18n().tr("Nord light");
    }
}
