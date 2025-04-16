/*
 * This file is part of the Autopo project
 * Created 05/02/25
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
module ooo.autopo.model {
    exports ooo.autopo.model;
    exports ooo.autopo.model.po;
    exports ooo.autopo.model.project;
    exports ooo.autopo.model.lifecycle;
    exports ooo.autopo.model.ui;
    exports ooo.autopo.model.io;
    exports ooo.autopo.model.ai;
    exports ooo.autopo.model.notification;
    exports ooo.autopo.model.ui.log;

    requires transitive com.soberlemur.potentilla;
    requires transitive atlantafx.base;
    requires ooo.autopo.i18n;
    requires org.sejda.commons;
    requires org.pdfsam.eventstudio;
    requires org.apache.commons.lang3;
    requires javafx.graphics;
    requires javafx.controls;
    requires org.kordamp.ikonli.fluentui;
    requires org.kordamp.ikonli.javafx;
    requires org.apache.commons.io;
    requires langchain4j.core;
    requires com.fasterxml.jackson.annotation;
    requires org.tinylog.api;
    opens ooo.autopo.model.ui to com.fasterxml.jackson.databind;

}