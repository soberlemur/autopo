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
    requires org.pdfsam.injector;
    requires jakarta.inject;
    requires org.tinylog.api;
    requires org.pdfsam.persistence;
    requires org.sejda.commons;
    requires javafx.graphics;
    requires langchain4j;
    requires langchain4j.core;
    requires java.net.http;
}