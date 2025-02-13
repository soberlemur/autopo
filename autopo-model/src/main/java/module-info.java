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
module ooo.autopo.model {
    exports ooo.autopo.model;
    exports ooo.autopo.model.lifecycle;
    exports ooo.autopo.model.ui;

    requires transitive com.soberlemur.potentilla;
    requires ooo.autopo.i18n;
    requires org.sejda.commons;
    requires org.pdfsam.eventstudio;
    requires org.apache.commons.lang3;
    requires javafx.graphics;
    requires org.kordamp.ikonli.fluentui;
    requires org.kordamp.ikonli.javafx;
    opens ooo.autopo.model.ui to com.fasterxml.jackson.databind;
}