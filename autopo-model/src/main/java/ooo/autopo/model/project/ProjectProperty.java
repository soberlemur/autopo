package ooo.autopo.model.project;

/*
 * This file is part of the Autopo project
 * Created 13/02/25
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
public enum ProjectProperty {
    ID("ooo.autopo.id"),
    NAME("ooo.autopo.name"),
    DESCRIPTION("ooo.autopo.description"),
    LOCALE("ooo.autopo.locale"),
    TEMPLATE_PATH("ooo.autopo.template.path"),
    EXCLUDE("ooo.autopo.exclude");

    private final String key;

    ProjectProperty(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}