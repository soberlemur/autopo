import ooo.autopo.model.ai.AIModelDescriptor;

/*
 * This file is part of the Autopo project
 * Created 14/02/25
 * Copyright 2025 by Sober Lemur S.r.l. (info@soberlemur.com).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
    requires langchain4j.anthropic;

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
    uses AIModelDescriptor;

    provides org.tinylog.writers.Writer with ooo.autopo.app.ui.logs.BroadcastingWriter;
}