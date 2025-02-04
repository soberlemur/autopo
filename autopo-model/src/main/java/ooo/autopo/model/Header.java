package ooo.autopo.model;

/*
 * This file is part of the Autopo project
 * Created 31/01/25
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
 * Represents the possible headers values
 *
 * @author Andrea Vacondio
 */
public enum Header {
    PROJECT_ID_VERSION("Project-Id-Version"),
    REPORT_MSGID_BUGS_TO("Report-Msgid-Bugs-To"),
    POT_CREATION_DATE("POT-Creation-Date"),
    PO_REVISION_DATE("PO-Revision-Date"),
    LAST_TRANSLATOR("Last-Translator"),
    LANGUAGE_TEAM("Language-Team"),
    LANGUAGE("Language"),
    CONTENT_TYPE("Content-Type"),
    CONTENT_TRANSFER_ENCODING("Content-Transfer-Encoding"),
    PLURAL_FORMS("Plural-Forms");

    private final String value;

    Header(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}