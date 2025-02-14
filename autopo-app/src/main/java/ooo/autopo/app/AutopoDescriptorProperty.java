package ooo.autopo.app;

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

/**
 * @author Andrea Vacondio
 */
public enum AutopoDescriptorProperty {
    NAME("ooo.autopo.name"),
    HOME_URL("ooo.autopo.home.url"),
    HOME_LABEL("ooo.autopo.home.label"),
    VERSION("ooo.autopo.version"),
    COPYRIGHT("ooo.autopo.copyright"),
    VENDOR_URL("ooo.autopo.vendor.url"),
    LICENSE_NAME("ooo.autopo.license.name"),
    LICENSE_URL("ooo.autopo.license.url"),
    TRACKER_URL("ooo.autopo.tracker.url"),
    SUPPORT_URL("ooo.autopo.support.url"),
    SCM_URL("ooo.autopo.scm.url"),
    DONATE_URL("ooo.autopo.donate.url");
    public final String prop;

    AutopoDescriptorProperty(String prop) {
        this.prop = prop;
    }
}
