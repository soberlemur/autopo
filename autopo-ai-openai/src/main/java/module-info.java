import ooo.autopo.model.ai.AIModelDescriptor;

/*
 * This file is part of the Autopo project
 * Created 30/01/25
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
module ooo.autopo.ai.openai {
    exports ooo.autopo.ai.openai;

    requires ooo.autopo.model;
    requires langchain4j.core;
    requires langchain4j.open.ai;
    requires ooo.autopo.i18n;
    requires org.pdfsam.persistence;
    requires org.tinylog.api;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fluentui;
    requires javafx.controls;
    requires org.apache.commons.lang3;
    requires java.net.http;
    requires langchain4j.http.client;
    requires langchain4j.http.client.jdk;

    provides AIModelDescriptor with ooo.autopo.ai.openai.OpenAiModelDescriptor;
}