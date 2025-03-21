import ooo.autopo.model.ai.AIModelDescriptor;

/*
 * This file is part of the Autopo project
 * Created 30/01/25
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
module ooo.autopo.ai.gemini {
    exports ooo.autopo.ai.gemini;

    requires ooo.autopo.model;
    requires langchain4j.core;
    requires langchain4j.google.ai.gemini;
    requires ooo.autopo.i18n;
    requires org.pdfsam.persistence;
    requires org.tinylog.api;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fluentui;
    requires javafx.controls;
    requires org.apache.commons.lang3;

    provides AIModelDescriptor with ooo.autopo.ai.gemini.GeminiAiModelDescriptor;
}