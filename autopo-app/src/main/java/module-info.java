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
    exports ooo.autopo.app.ui;
    exports ooo.autopo.app.ui.editor;
    exports ooo.autopo.app.ui.project;
    exports ooo.autopo.app.ui.logs;
    exports ooo.autopo.app.ui.explorer;

    requires atlantafx.base;
    requires com.fasterxml.jackson.datatype.jdk8;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires jakarta.inject;
    requires java.desktop;
    requires net.synedra.validatorfx;
    requires ooo.autopo.ai.openai;
    requires ooo.autopo.i18n;
    requires ooo.autopo.model;
    requires ooo.autopo.service;
    requires ooo.autopo.themes;
    requires org.apache.commons.lang3;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.fluentui;
    requires org.kordamp.ikonli.javafx;
    requires org.pdfsam.eventstudio;
    requires org.pdfsam.injector;
    requires org.pdfsam.persistence;
    requires org.sejda.commons;
    requires org.slf4j;
    requires org.tinylog.api;
    requires org.tinylog.impl;

    opens ooo.autopo.app to org.pdfsam.injector, org.pdfsam.eventstudio;
    opens ooo.autopo.app.ui to org.pdfsam.injector, org.pdfsam.eventstudio;
    opens ooo.autopo.app.ui.about to org.pdfsam.injector;
    opens ooo.autopo.app.ui.logs to org.pdfsam.injector, org.pdfsam.eventstudio;
    opens ooo.autopo.app.ui.settings to org.pdfsam.injector;
    opens ooo.autopo.app.ui.editor to org.pdfsam.injector, org.pdfsam.eventstudio;
    opens ooo.autopo.app.config to org.pdfsam.injector;
    opens ooo.autopo.app.ui.notification to org.pdfsam.injector, org.pdfsam.eventstudio;
    opens ooo.autopo.app.ui.explorer to org.pdfsam.eventstudio, org.pdfsam.injector;
    opens ooo.autopo.app.ui.project to org.pdfsam.injector;

    uses ooo.autopo.theme.Theme;
    uses ooo.autopo.model.ai.AiModelDescriptor;

    provides org.tinylog.writers.Writer with ooo.autopo.app.ui.logs.BroadcastingWriter;
}