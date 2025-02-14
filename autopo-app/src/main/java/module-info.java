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
module ooo.autopo.app {
    exports ooo.autopo.app;

    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires javafx.controls;
    requires com.fasterxml.jackson.datatype.jdk8;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires ooo.autopo.service;
    requires org.pdfsam.injector;
    requires jakarta.inject;
    requires org.tinylog.api;
    requires org.pdfsam.persistence;
    requires org.sejda.commons;
    requires ooo.autopo.i18n;
    requires org.apache.commons.lang3;
    requires org.pdfsam.eventstudio;
    requires java.desktop;
    requires org.kordamp.ikonli.fluentui;
    requires org.kordamp.ikonli.core;

    opens ooo.autopo.app to org.pdfsam.injector, org.pdfsam.eventstudio;
    opens ooo.autopo.app.ui to org.pdfsam.injector;
    opens ooo.autopo.app.ui.about to org.pdfsam.injector;
    opens ooo.autopo.app.config to org.pdfsam.injector;
    opens ooo.autopo.app.ui.notification to org.pdfsam.injector, org.pdfsam.eventstudio;
}