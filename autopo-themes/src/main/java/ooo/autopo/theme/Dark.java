package ooo.autopo.theme;
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

import java.util.ArrayList;
import java.util.List;

import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * @author Andrea Vacondio
 */
public class Dark extends Light {

    @Override
    public String id() {
        return "KDJ4FJ49D46H09JV1";
    }

    @Override
    public List<String> stylesheets() {
        var styles = new ArrayList<>(super.stylesheets());
        styles.add("/themes/dark/colors.css");
        styles.add("/themes/dark/theme.css");
        return styles;
    }

    @Override
    public String name() {
        return i18n().tr("Dark with purple");
    }

    @Override
    public boolean isDark() {
        return true;
    }
}
