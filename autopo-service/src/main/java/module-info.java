/*
 * This file is part of the Autopo project
 * Created 05/02/25
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
module ooo.autopo.service {
    exports ooo.autopo.service;
    exports ooo.autopo.service.io;
    exports ooo.autopo.service.ai;
    exports ooo.autopo.service.ui;
    exports ooo.autopo.service.project;

    requires com.soberlemur.potentilla;
    requires ooo.autopo.i18n;
    requires org.pdfsam.eventstudio;
    requires transitive ooo.autopo.model;
    requires org.apache.commons.lang3;
    requires javafx.graphics;
    requires org.pdfsam.injector;
    requires jakarta.inject;
    requires org.tinylog.api;
    requires org.pdfsam.persistence;
    requires org.sejda.commons;
}