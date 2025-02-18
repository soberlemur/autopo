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

import java.util.List;

import static ooo.autopo.i18n.I18nContext.i18n;

/**
 * @author Andrea Vacondio
 */
public class Light implements Theme {

    @Override
    public String id() {
        return "AS876FDS7RB3";
    }

    @Override
    public List<String> stylesheets() {
        return List.of("/themes/light/colors.css",
                       "/themes/light/theme.css",
                       "/themes/light/tooltip.css",
                       "/themes/light/footer.css",
                       "/themes/light/list.css",
                       "/themes/light/dialogs.css",
                       "/themes/light/scrollbars.css",
                       "/themes/light/notifications.css",
                       "/themes/light/about.css",
                       "/themes/light/menu.css",
                       "/themes/light/table.css",
                       "/themes/light/fileexplorer.css",
                       "/themes/light/logs.css",
                       "/themes/light/combo.css",
                       "/themes/light/theme.last.css");
    }

    @Override
    public List<String> transparentIncapableStylesheets() {
        return List.of("/themes/light/transparent-incapable.css");
    }

    @Override
    public String name() {
        return i18n().tr("Light with blue");
    }

    @Override
    public boolean isDark() {
        return false;
    }
}
